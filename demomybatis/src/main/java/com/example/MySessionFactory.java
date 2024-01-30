package com.example;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MySessionFactory {
    private static volatile SqlSessionFactory sqlSessionFactory = null;

    public static SqlSessionFactory getSessionFactory() throws IOException {
        if(sqlSessionFactory == null){
            synchronized (MySessionFactory.class){
                if(sqlSessionFactory == null){
                    String resource = "mybatis-config.xml";
                    InputStream inputStream = Resources.getResourceAsStream(resource);
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                }
            }
        }
        return sqlSessionFactory;
    }
}
