package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    public void importItemData() {
//审核通过的才导入的

        //从数据库中提取规格json字符串转换为map
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");

        List<TbItem> tbItems = itemMapper.selectByExample(example);

        for (TbItem tbItem : tbItems) {
            Map specMap = JSON.parseObject(tbItem.getSpec());
            tbItem.setSpecMap(specMap);
        }
        showList(tbItems);

        solrTemplate.saveBeans(tbItems);
        solrTemplate.commit();


        System.out.println("---结束---");
        }

    public void showList(List<TbItem> itemList){
        for (TbItem item : itemList) {
            System.out.println(item.getTitle()+"  " +item.getPrice());
        }

    }


    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();

    }

}
