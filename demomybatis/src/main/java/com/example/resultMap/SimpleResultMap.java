package com.example.resultMap;

import com.example.MySessionFactory;
import com.example.mapper.Blog;
import com.example.mapper.BlogMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;

/**
 * 定义ResultMap来接收返回结果
 *    <select id="selectBlogSimpleResultMap" resultMap="blogResultMap" parameterType="long">
 *         SELECT author,NOW() as selectTimedb from Blog where id = #{id}
 *     </select>
 *
 *         <resultMap id="blogResultMap" type="com.example.simpleBean.BlogSimple">
 *         <constructor>
 *             <arg column="author" javaType="string"></arg>
 *             <arg column="selectTimedb" javaType="date"/>
 *         </constructor>
 *         <result column="author" property="author" javaType="string"/>
 *         <!--jdbcType 如果你直接面向 JDBC 编程，你需要对可能存在空值的列指定这个类型。-->
 *         <result column="selectTimedb" property="selectTime" javaType="date"/>
 *     </resultMap>
 */
public class SimpleResultMap {
    public static void main(String[] args) throws IOException {
        SqlSession sqlSession =  MySessionFactory.getSessionFactory().openSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        System.out.println(mapper.selectBlogSimpleResultMap(1l));
    }
}
