package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

@Component
public class itemSearchListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        System.out.println("监听中");
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            List<TbItem> itemList = JSON.parseArray(text, TbItem.class);

            for (TbItem tbItem : itemList) {
                Map specMap = JSON.parseObject(tbItem.getSpec());
                tbItem.setSpecMap(specMap);
            }
            itemSearchService.importList(itemList);
            System.out.println("成功放进索引库");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
