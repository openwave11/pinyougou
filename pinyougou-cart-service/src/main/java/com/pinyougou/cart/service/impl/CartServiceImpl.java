package com.pinyougou.cart.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service(timeout = 5000, retries = 0)
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    /**
     * //添加到购物车
     */
    public List<Cart> addGoodsToCartList(List<Cart> catList, Long itemId, Integer num) {

        for (int i = 0; i < 10; i++) {
            continue;
        }
        //添加到购物车
        //1.根据skuID查询商品明细SKU的对象
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        if (tbItem == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!"1".equals(tbItem.getStatus())) {
            throw new RuntimeException("状态码不正确");
        }
        //2.根据SKU对象得到商家ID
        String sellerId = tbItem.getSellerId();
        //3.根据商家ID在购物车列表中查询购物车对象
        Cart cart = searchCartBySellerId(catList, sellerId);

        if (cart == null) {
            //4.如果购物车列表中不存在该商家的购物车
            //4.1 创建一个新的购物车对象

            Cart cartNew = new Cart();
            cartNew.setSellerId(sellerId);
            cartNew.setSellerName(tbItem.getSeller());

            TbOrderItem orderItem = createOrderItem(tbItem, num);
            ArrayList<TbOrderItem> orderItemList = new ArrayList<TbOrderItem>();
            orderItemList.add(orderItem);
            cartNew.setOrderItemList(orderItemList);
            //4.2 将新的购物车对象添加到购物车列表中
            catList.add(cartNew);

        } else {
            //5.如果购物车列表中存在该商家的购物车
            //5.1 如果不存在  ，创建新的购物车明细对象，并添加到该购物车的明细列表中
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (orderItem == null) {
                TbOrderItem tbOrderItem = createOrderItem(tbItem, num);
                cart.getOrderItemList().add(tbOrderItem);

            } else {
                //5.2 如果存在，在原有的数量上添加数量 ,并且更新金额
                orderItem.setNum(orderItem.getNum() + num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * tbItem.getPrice().doubleValue()));

                //如果数量小于0，移除
                if (orderItem.getNum() <= 0) {
                    cart.getOrderItemList().remove(orderItem);
                }
            }
            //如果列表数量小于0.删除cart
            if (cart.getOrderItemList().size() <= 0) {
                catList.remove(cart);
            }
        }
        return catList;
    }

    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("从redis中获取数据");
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList == null) {
            cartList = new ArrayList<>();
        }
        System.out.println("从redis中获取数据是:"+cartList);
        return cartList;
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        System.out.println("从redis中存入数据");
        redisTemplate.boundHashOps("cartList").put(username, cartList);

    }

    @Override
    /**
     * 合并购物车
     */
    public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
        System.out.println("合并购物车");
        for (Cart cart : cartList2) {

            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                cartList1 = addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
            }
        }

        return cartList1;
    }


    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().equals(itemId)) {
                return orderItem;
            }
        }
        return null;
    }

    private TbOrderItem createOrderItem(TbItem item, Integer num) {
        if (num <= 0) {
            throw new RuntimeException("数量非法");
        }
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num.doubleValue()));
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(item.getSellerId());
        return orderItem;
    }


    /**
     * 判断该商家id是否在已有列表存在
     *
     * @param catList
     * @param sellerId
     * @return
     */
    private Cart searchCartBySellerId(List<Cart> catList, String sellerId) {
        for (Cart cart : catList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }


    //购物车列表合并
    //    1. 从Cookie中获取购物车列表
    //    2. 从Reids中获取购物车列表
    //    3. 调用service层的方法完成购物车列表的合并，返回的就是一个合并以后的购物车列表
    //    4. 把最新的购物车列表重新存储到Redis中
    //    5. 把Cookie中的购物车列表进行删除


}