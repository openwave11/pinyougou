package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.util.CookieUtil;
import com.pinyougou.pojogroup.Cart;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 6000, retries = 0)
    private CartService cartService;



    @Autowired
    private HttpServletRequest request;


    @Autowired
    private HttpServletResponse response;
    //添加到购物车
    //// 1. 从Cookie中获取购物车列表数据
    //// 2. 调用service完成商品的添加操作，返回的就是最新的购物车列表数据
    //// 3. 把最新的购物车列表数据存储到Cookie中

    /**
     * 从cookie获取购物车列表
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        String cartListString = CookieUtil.getCookieValue(request, "cartList", "utf-8");
        if (cartListString == null || "".equals(cartListString)) {
            cartListString = "[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);

        if ("anonymousUser".equals(username)) {
            //如果未登录从cookie中获取
            return cartList_cookie;
        } else {
            //如果已经登录从redis中获取
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);

            if (cartList_cookie.size() > 0) {
                //合并
                cartList_redis = cartService.mergeCartList(cartList_redis, cartList_cookie);
                //删除本地redis
                CookieUtil.deleteCookie(request, response, "cartList");
                //清除redis
                cartService.saveCartListToRedis(username, cartList_redis);
            }
            return cartList_redis;
        }
    }


    @RequestMapping("/addGoodsToCartList")
    //在方法上添加crossOrigin,实现跨域
    @CrossOrigin(origins = "http://localhost:9105",allowCredentials = "true")
    public Result addGoodsToCartList(Long itemId, Integer num) {

    /*    //可以访问的域(当此方法不需要cookie)
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");

        //如果操作cookie，必须加上这句话
        response.setHeader("Access-Control-Allow-Credentials", "true");
*/



        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("登录用户为：" + username);
        try {
            List<Cart> cartList = findCartList();
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            if ("anonymousUser".equals(username)) {
                //如果未登录添加到cookie中
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600, "utf-8");
            } else {
                //如果已经登录保存到redis中
                cartService.saveCartListToRedis(username, cartList);
            }
            return new Result(true, "添加购物车成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }

    }





}
