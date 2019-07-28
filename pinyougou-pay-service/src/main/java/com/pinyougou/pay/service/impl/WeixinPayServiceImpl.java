package com.pinyougou.pay.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.util.HttpClient;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;


@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    @Value("${notifyurl}")
    private String notifyurl;

    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        //1.参数封装
        Map param = new HashMap();
        //公众账号ID
        param.put("appid", appid);
        //商户
        param.put("partner", partner);
        //随机字符串
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        param.put("body", "品优购");
        //商户订单号
        param.put("out_trade_no", out_trade_no);
        //交易订单号
        //金额（分）
        param.put("total_fee", total_fee);
        //终端IP
        param.put("spbill_create_ip", "127.0.0.1");
        //通知地址
        param.put("notify_url", notifyurl);
        //交易类型
        param.put("trade_type", "NATIVE");

        try {
            //2.发送请求
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            System.out.println(xmlParam);
            HttpUtil.get("https://api.mch.weixin.qq.com/pay/unifiedorder", param);
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setHttps(true);
            client.setXmlParam(xmlParam);
            client.post();

            //3.获取结果
            String result = client.getContent();
            System.out.println("result:" + result);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            HashMap<String, String> map = new HashMap<>();
//            String appid = map.get("appid");
//            String mchId = map.get("mch_id");
//            String nonceStr = map.get("nonce_str");
//            String sign = map.get("sign");
//            String resultCode = map.get("result_code");
//            String tradeType = map.get("trade_type");
//            String prepayId = map.get("prepay_id");
            String errCode = resultMap.get("err_code");
            System.out.println("errCode:"+errCode);

            map.put("code_url", resultMap.get("code_url"));
            map.put("total_fee", total_fee);
            map.put("out_trade_no", out_trade_no);
            return map;


        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }


    }

    @Override
    public Map queryPayStatus(String out_trade_no) {
        //1.封装参数
        Map param = new HashMap();
        param.put("appid", appid);
        param.put("mch_id", partner);
        param.put("out_trade_no", out_trade_no);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            String xmlParam = WXPayUtil.generateSignedXml(param, partnerkey);
            //2.发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParam);
            httpClient.post();

            //3.获取结果
            String xmlResult = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
            System.out.println("调动查询API返回结果：" + xmlResult);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
