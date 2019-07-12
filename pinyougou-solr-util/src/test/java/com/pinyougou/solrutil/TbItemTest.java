package com.pinyougou.solrutil;


import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(value = {"classpath:spring/applicationContext-solr.xml","classpath:spring/applicationContext.xml"})
//@ContextConfiguration("classpath:ApplicationContext.xml")
@ContextConfiguration("classpath:spring/applicationContext-solr.xml")
public class TbItemTest {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testAdd() throws IOException {

        String fileName = "C:\\develop\\IdeaProject\\pinyougou\\pinyougou-parent\\pinyougou-solr-util\\target\\classes\\log4j.properties";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line ;
        while ((line=(br.readLine()))!=null){
            System.out.println(line);
        }
        br.close();


    }

    @Test
    public void pageDeleAll() {
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();


    }

    @Test
    public void pageQueryList() {
        SimpleQuery query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria();
//        Criteria criteria = new Criteria("item_title").contains("为");
//        criteria.and("item_title").contains("5");
        criteria.fuzzy("化为");
        query.addCriteria(criteria);

        query.setOffset(0);
        query.setRows(200);
        Page<TbItem> items = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总页数" + items.getTotalPages());
        System.out.println("总记录数" + items.getTotalElements());

        List<TbItem> list = items.getContent();
        showList(list);


    }


    @Test
    public void page() {
        SimpleQuery query = new SimpleQuery("*:*");
        query.setOffset(0);
        query.setRows(150);

        Page<com.pinyougou.pojo.TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        if (tbItems == null) {
            System.out.println("空指针了！！！");
        }
        List<TbItem> list = tbItems.getContent();
        System.out.println("总数量" + tbItems.getTotalElements());
        showList(list);


    }

    @Test
    public void addList() {
        ArrayList<TbItem> list = new ArrayList<>();
        for (int i = 101; i < 200; i++) {
            TbItem item = new TbItem();
            item.setId(i + 1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(i + 1L);
            item.setSeller("华为" + i + "号专卖店");
            item.setTitle("华为Mate" + i);
            item.setPrice(new BigDecimal(2000 + i));
            list.add(item);

        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();

    }

    @Test
    public void deleteOne() {
        solrTemplate.deleteById("1");
        solrTemplate.commit();

    }

    @Test
    public void findOne() {

        TbItem item = solrTemplate.getById(1, TbItem.class);
        System.out.println(item);

    }


    @Test
    public void saveItem() {
        TbItem item = new TbItem();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    public void showList(List<TbItem> itemList) {
        for (TbItem item : itemList) {
            System.out.println(item.getTitle() + "  " + item.getPrice());
        }

    }


}
