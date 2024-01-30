package com.example.mapper;

import com.example.simpleBean.BlogSimple;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BlogMapper {
    Blog selectBlog(long id);
    Map<String,Object> selectBlogMap(long id);
    BlogSimple selectBlogSimpleResultMap(long id);
    void insertOne(@Param("author") String author, @Param("date")Date date, @Param("title")String title, @Param("content")String content);
    void insertOneByBean(@Param("b")Blog b);
    void insertMany(@Param("objList") List<Map<String,Object>> objList);

    /**
     * 使用Object来作为返回结果，这也不行。不会根据查询到的结果而返回，而是默认了selectOne，有多个会报错
     */
    @Select("select * from Blog where ${column} = #{value}")
    Blog findByColumn(@Param("column") String column, @Param("value") String value);
}
