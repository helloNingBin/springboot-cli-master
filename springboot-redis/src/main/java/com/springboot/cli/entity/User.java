package com.springboot.cli.entity;


import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * @author ding
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 性别
     */
    private String sex;

    /**
     * 备注
     */
    private String remark;
}
