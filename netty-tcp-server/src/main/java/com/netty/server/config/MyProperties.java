package com.netty.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@ConfigurationProperties("my")
public class MyProperties {
    private List<ExecutorProperty> executorPropertyList;

    public static class ExecutorProperty{
        String name;
        int coreNum;
        int maxNum;

    }
}
