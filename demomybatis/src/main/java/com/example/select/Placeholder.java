package com.example.select;

import com.example.MySessionFactory;
import com.example.mapper.BlogMapper;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;

/**
 * ${column}只是普通占位符，#{value}会使用 ? 预处理防止sql攻击
 * @Select("select * from user where ${column} = #{value}")
 * User findByColumn(@Param("column") String column, @Param("value") String value);
 *
 * 定义一个方法，可以用任意列作为查询条件
 */
public class Placeholder {

    public static void main(String[] args) throws IOException {
        SqlSession sqlSession =  MySessionFactory.getSessionFactory().openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        System.out.println(mapper.findByColumn("author","authorList"));
    }
}
