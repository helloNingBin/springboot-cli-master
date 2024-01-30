package com.example.insert;

import com.example.MySessionFactory;
import com.example.mapper.Blog;
import com.example.mapper.BlogMapper;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.Date;

/**
 * 用Bean作为参数
 * parameterType也可以不填写
 *  <insert id="insertOneByBean" parameterType="blog">
 *         INSERT INTO Blog(author,date,title,content) values(#{b.author},#{b.date},#{b.title},#{b.content})
 *     </insert>
 */
public class insertOneByBean {
    public static void main(String[] args) throws IOException {
        SqlSession sqlSession =  MySessionFactory.getSessionFactory().openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog b = new Blog(new Date(), "insertOneByBean author", "insertOneByBean title ", "insertOneByBean content");
        mapper.insertOneByBean(b);
        sqlSession.commit();
    }
}
