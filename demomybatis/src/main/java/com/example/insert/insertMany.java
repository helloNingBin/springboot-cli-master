package com.example.insert;

import com.example.MySessionFactory;
import com.example.mapper.BlogMapper;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.*;

/**
 * 插入多个，这次用List Map来演示，其实用其它实体也是可以的
 *  <insert id="insertMany">
 *         INSERT INTO Blog(author,date,title,content) values
 *         <foreach collection="objList" item="b" separator=",">
 *             <!--separator="," 表示每次迭代用它来隔开-->
 *             (#{b.author},#{b.date},#{b.title},#{b.content})
 *         </foreach>
 *     </insert>
 */
public class insertMany {
    public static void main(String[] args) throws IOException {
        SqlSession sqlSession =  MySessionFactory.getSessionFactory().openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        List<Map<String,Object>> objList = new ArrayList<>();
        Map<String,Object> m1 = new HashMap<>();
        m1.put("author", "authorList");
        m1.put("date", new Date());
        //.title},#{b.content})
        m1.put("title", "title list");
//        m1.put("content", "content list");
        objList.add(m1);objList.add(m1);objList.add(m1);
        mapper.insertMany(objList);
        sqlSession.commit();
    }
}
