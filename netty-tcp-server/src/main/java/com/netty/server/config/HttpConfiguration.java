package com.netty.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConfiguration {
    @Bean
    public RestTemplate buildRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        // 引入其他配置
        return restTemplate;
    }
}
