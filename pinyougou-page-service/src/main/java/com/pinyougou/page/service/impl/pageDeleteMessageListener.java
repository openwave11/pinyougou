package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class pageDeleteMessageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;

            System.out.println("PageDeleteMessageListener is Runinggggggggggggg");
            Long[] goodIds = (Long[])objectMessage.getObject();
            boolean b = itemPageService.deleItemHtml(goodIds);
            System.out.println("删除结果deleteResult："+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
