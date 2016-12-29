package com.my.mybatis.plugins.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.my.mybatis.plugins.model.User;

/**
 * @Author: wangtian
 */

public interface UserMapper {

    public List<User> queryUsersByName(@Param("name") String name);
}
