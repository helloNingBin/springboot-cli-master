package com.example;

import com.example.mapper.Blog;
import com.example.mapper.BlogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 从 XML 中构建 SqlSessionFactory
 * 每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。
 * SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得。
 * 而 SqlSessionFactoryBuilder 则可以从 XML 配置文件来构建出SqlSessionFactory实例。
 *
 *遇到一个坑：包名要用"."的才能找到，不知为什么？
 *        ###现在知道了，那是因为建目录时直接com.example像建class那样
 *            但建目录并不会递归创建，而是把com.example当作是目录名称####
 *   <mappers>
 *         <mapper resource="com.example.mapper/BlogMapper.xml"/>
 *     </mappers>
 *
 * 1.BlogMapper.xml是放在resources下面的
 * 2.BlogMapper的路径是在BlogMapper.xml里的namespace配置的
 *   <mapper namespace="com.example.mapper.BlogMapper">
 * 3.BlogMapper接口的方法要和BlogMapper.xml中的对应起来
 */
public class HelloWord {
    public static void main(String[] args) throws IOException {
        SqlSessionFactory sqlSessionFactory = MySessionFactory.getSessionFactory();
        System.out.println(sqlSessionFactory);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        System.out.println(mapper);
        Blog blog = mapper.selectBlog(1);
        System.out.println(blog);
        System.out.println(mapper.selectBlogMap(1));
    }
}
