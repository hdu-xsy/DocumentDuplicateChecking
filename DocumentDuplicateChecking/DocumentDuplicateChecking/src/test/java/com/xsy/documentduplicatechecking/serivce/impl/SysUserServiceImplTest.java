package com.xsy.documentduplicatechecking.serivce.impl;

import com.xsy.documentduplicatechecking.serivce.SysUserService;
import com.xsy.documentduplicatechecking.utils.MD5Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SysUserServiceImplTest {

    @Resource
    private SysUserService sysUserService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findByToken() {
    }

    @Test
    public void findCodeByMobileNo() {
    }

    @Test
    public void removeCode() {
    }

    @Test
    public void saveUser() {
    }

    @Test
    public void loginUser() throws Exception{
        //System.out.println(MD5Util.getMD5Format(MD5Util.getMD5Format("111111") + "fe0c7c"));
        String token = sysUserService.loginUser("18768112602","111111");
        System.out.println(token);
    }

}