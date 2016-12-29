package com.my.mybatis.plugins;

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

    //case slow sql
}
