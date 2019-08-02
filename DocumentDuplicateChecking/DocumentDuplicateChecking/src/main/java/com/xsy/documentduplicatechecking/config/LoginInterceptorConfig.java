package com.xsy.documentduplicatechecking.config;

import com.xsy.documentduplicatechecking.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * token拦截器
 *
 * @author xushiyue
 * @date 2019年7月30日17:22:17
 */
@Configuration
public class LoginInterceptorConfig extends WebMvcConfigurerAdapter {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/error", "/login", "/reg", "/test", "/sendRegMsg");
        super.addInterceptors(registry);
    }
}
