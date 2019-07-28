package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

public interface CartService {

    /**
     * 添加商品到购物车
     * @param catList
     * @param itemId
     * @param num
     * @return
     */
    public abstract List<Cart> addGoodsToCartList(List<Cart> catList, Long itemId, Integer num);

    public List<Cart> findCartListFromRedis(String username);


    public void saveCartListToRedis(String username, List<Cart> cartList);

    public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
}
