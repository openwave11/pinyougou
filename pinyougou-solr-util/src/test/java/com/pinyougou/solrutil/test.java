package com.pinyougou.solrutil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext*.xml")
public class test {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {


//        redisTemplate.opsForValue().set("num","123");
//        Object cartList = redisTemplate.boundHashOps("cartList").getKey();
//        System.out.println("111111111："+cartList);

//        List<String> keys = new ArrayList<>();
//        List list = redisTemplate.opsForValue().multiGet(keys);
//        System.out.println("111111111："+list);
//        Object o = redisTemplate.boundHashOps("cartList").get("lijialong");
//        System.out.println(o);
    }

    @Test
    public void test2() {
        Boolean content = redisTemplate.expire("nameSet", 1, TimeUnit.SECONDS);

        System.out.println(content);
//        Set nameSet = redisTemplate.boundHashOps("nameSet").keys();

    }

}
