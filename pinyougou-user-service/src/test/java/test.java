/*import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class test {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        Boolean content = redisTemplate.expire("nameSet", 1, TimeUnit.SECONDS);

        System.out.println(content);
//        Set nameSet = redisTemplate.boundHashOps("nameSet").keys();

    }
}*/
