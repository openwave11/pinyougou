package com.pinyougou.sellergoods.service.impl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath*:spring/appli*.xml")
public class BrandServiceImplTest {
//    @Autowired
//    private BrandService brandService;

    @Test
    public void testFindAll() throws JsonProcessingException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("www");
        System.out.println(encode);
    }
}


