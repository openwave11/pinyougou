package com.pinyougou.cart.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    public void ss(){
        System.out.println(1);
    }
    //添加到购物车
    //1.根据skuID查询商品明细SKU的对象
    //2.根据SKU对象得到商家ID
    //3.根据商家ID在购物车列表中查询购物车对象
    //4.如果购物车列表中不存在该商家的购物车
    //4.1 创建一个新的购物车对象
    //4.2 将新的购物车对象添加到购物车列表中
    //5.如果购物车列表中存在该商家的购物车
    //5.1 如果不存在  ，创建新的购物车明细对象，并添加到该购物车的明细列表中
    //5.2 如果存在，在原有的数量上添加数量 ,并且更新金额




    //购物车列表合并
    //    1. 从Cookie中获取购物车列表
    //    2. 从Reids中获取购物车列表
    //    3. 调用service层的方法完成购物车列表的合并，返回的就是一个合并以后的购物车列表
    //    4. 把最新的购物车列表重新存储到Redis中
    //    5. 把Cookie中的购物车列表进行删除
}

