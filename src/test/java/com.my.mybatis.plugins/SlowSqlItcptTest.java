package com.my.mybatis.plugins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.my.mybatis.plugins.dao.UserMapper;

/**
 * @Author: wangtian
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring.xml")
public class SlowSqlItcptTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * case slow sql
     * set executeTimeThreshold = 1
     * if query sql excute time > 1ms,create warn log
     */
    @Test
    public void slowSqllog(){
        String userName = "test";
        userMapper.queryUsersByName(userName);
    }
}
