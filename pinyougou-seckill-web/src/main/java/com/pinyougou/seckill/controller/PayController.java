package com.pinyougou.seckill.controller;

import com.pinyougou.common.util.IdWorker;
import com.pinyougou.pay.service.WeixinPayService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private WeixinPayService weixinPayService;

    /**
     * 生成二维码
     *
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    @RequestMapping("/createNative")
    public Map createNative(String out_trade_no, String total_fee) {

        IdWorker idWorker = new IdWorker();

        return weixinPayService.createNative(idWorker.nextId() + "", "1");
    }

    /*** 查询支付状态* @param out_trade_no* @return*/
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        Result result = null;
        while (true) {
            Map map = weixinPayService.queryPayStatus(out_trade_no);
            if (map == null) {
                result = new Result(false, "支付出错");
                break;
            }
            if (map.get("trade_state").equals("SUCCESS")) {
                result = new Result(true, "支付成功");
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
