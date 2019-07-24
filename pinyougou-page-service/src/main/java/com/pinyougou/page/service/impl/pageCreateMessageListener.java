package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class pageCreateMessageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            System.out.println("PageCreateMessageListener is Runinggggggggggggg");

            Long[] goodsIds = (Long[]) objectMessage.getObject();

            boolean b = false;
            for (Long goodsId : goodsIds) {
                 b = itemPageService.getItemHtml(goodsId);
            }

            System.out.println("添加结果PageCreateMessageListener_____Result："+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
