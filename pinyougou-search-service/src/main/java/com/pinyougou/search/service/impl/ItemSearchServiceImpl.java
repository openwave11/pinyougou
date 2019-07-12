package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Override
    public  Map<String, Object> search(Map specMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(searchList(specMap));

        List list = searchCategoryList(specMap);

        map.put("categoryList", list);
        return map;
    }

    private  Map<String, Object> searchList(Map specMap){
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
        Criteria criteria = new Criteria("item_keywords").is(specMap.get("keywords"));

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

        return map;
    }

    /**
     * 根据搜索关键字查询商品分类名称列表
     * @param searchMap
     * @return
     */
    private List searchCategoryList(Map searchMap){
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

}
