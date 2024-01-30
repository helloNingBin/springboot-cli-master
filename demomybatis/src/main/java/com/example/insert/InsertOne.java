package com.example.insert;

import com.example.MySessionFactory;
import com.example.mapper.BlogMapper;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.Date;

/**
 * 传入基本参数，但接口中要使用@Param，@Param("author") String author
 * 并且没有返回值，即使是Object也报错
 * <insert id="insertOne">
 *         INSERT INTO Blog(author,date,title,content) values(#{author},#{date},#{title},#{content})
 *     </insert>
 */
public class InsertOne {
    public static void main(String[] args) throws IOException {
        SqlSession sqlSession =  MySessionFactory.getSessionFactory().openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        mapper.insertOne("333", new Date(), "tile3", "content4");
        sqlSession.commit();
    }
}
