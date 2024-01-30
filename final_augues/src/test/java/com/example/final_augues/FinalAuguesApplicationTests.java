package com.example.final_augues;

import com.base.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class FinalAuguesApplicationTests {
    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        System.out.println(context);
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        System.out.println(Integer.toHexString(24456));
    }

}
