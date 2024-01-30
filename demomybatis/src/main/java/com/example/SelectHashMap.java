package com.example;

import com.example.mapper.Blog;
import com.example.mapper.BlogMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;

/**
 * 让数据以HashMap形式返回
 *重点在resultType，会自动封装成HashMap
 *    <select id="selectBlogMap" resultType="hashmap">
 *         select * from Blog where id = #{id}
 *     </select>
 */
public class SelectHashMap {
    public static void main(String[] args) throws IOException {
        SqlSessionFactory sqlSessionFactory = MySessionFactory.getSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        System.out.println(mapper.selectBlogMap(1));
    }
}
