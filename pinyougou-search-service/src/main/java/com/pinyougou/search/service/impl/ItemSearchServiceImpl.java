package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service(timeout = 5000, retries = 0)
@Transactional
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        //高亮显示
        map.putAll(searchList(searchMap));
        //根据关键字查询商品分类
        List<String> categorylist = searchCategoryList(searchMap);
        map.put("categoryList", categorylist);

        //查询品牌和规格列表
        //如果有分类名称。则查询点击的分类名称
        //如果没有，则按第一个查询
        String categoryName= (String) searchMap.get("category");
            if (!"".equals(categoryName)){
                map.putAll(searchBrandAndSpecList(categoryName));
            }else {
                map.putAll(searchBrandAndSpecList(categorylist.get(0)));
            }

       /* if (categorylist.size()>0){
            Map brandAndSpecList = searchBrandAndSpecList(categorylist.get(0));
            map.putAll(brandAndSpecList);
        }*/

        return map;
    }

    /**
     * 实现关键词高亮
     *
     * @param searchMap
     * @return
     */
    private Map<String, Object> searchList(Map searchMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        //关键词高亮显示
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        HighlightOptions options = new HighlightOptions().addField("item_title");

//        options.setSimplePrefix("<em style='color:red'>");
        options.setSimplePrefix("<em style='color:red'>");
        options.setSimplePostfix("</em>");

        query.setHighlightOptions(options);

        //按照关键字查询
//        SimpleQuery query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));

        query.addCriteria(criteria);

        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        for (HighlightEntry<TbItem> h : page.getHighlighted()) {
            TbItem item = h.getEntity();
            if (h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
            }
        }

        System.out.println("总数量" + page.getTotalElements());

        List<TbItem> list = page.getContent();

        map.put("rows", list);


        //1.2按分类筛选
        if (!"".equals(searchMap.get("category"))){
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.3按品牌筛选
        if (!"".equals(searchMap.get("brand"))){
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.4按规格筛选
        if (searchMap.get("spec")!= null){
            Map<String,String> specMap = (Map<String, String>) searchMap.get("spec");

            for (String key : specMap.keySet()) {
                Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        return map;
    }

    /**
     * 根据搜索关键字查询商品分类名称列表
     *
     * @param searchMap
     * @return
     */
    private List searchCategoryList(Map searchMap) {
        List<String> list = new ArrayList<>();
        SimpleQuery query = new SimpleQuery();

        //设置条件
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //设置分组
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);

        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");

        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();

        List<GroupEntry<TbItem>> groupEntryList = groupEntries.getContent();

        for (GroupEntry<TbItem> tbItemGroupEntry : groupEntryList) {
            String groupValue = tbItemGroupEntry.getGroupValue();
            list.add(groupValue);
        }
        return list;
    }


    private Map searchBrandAndSpecList(String category) {
        Map map = new HashMap<>();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);

        if (typeId != null) {
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("brandList", brandList);
            map.put("specList", specList);
        }
        return map;
    }


}
