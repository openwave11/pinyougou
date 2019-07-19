package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class itemDeleteListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
//        itemSearchService.deleteByGoodsIds(ids);
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            Long[] goodsId = (Long[]) objectMessage.getObject();
            System.out.println("监听消息"+goodsId);
            itemSearchService.deleteByGoodsIds(goodsId);
            System.out.println("删除商品");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
