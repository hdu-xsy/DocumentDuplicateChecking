package com.xsy.documentduplicatechecking.interceptor;

import com.xsy.documentduplicatechecking.entity.HostHolder;
import com.xsy.documentduplicatechecking.entity.SysUser;
import com.xsy.documentduplicatechecking.serivce.SysUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xushiyue
 * @date 2019年7月30日17:11:10
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private HostHolder hostHolder;

    @Resource
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Cookie[] cookies = httpServletRequest.getCookies();
        String userId = null;
        if (cookies == null) {
            httpServletResponse.sendRedirect("/error");
            return false;
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                userId = sysUserService.findByToken(cookie.getValue());
                break;
            }
        }
        if (userId == null) {
            httpServletResponse.sendRedirect("/error");
            return false;
        }
        SysUser sysUser = new SysUser();
        sysUser.setPassword("aaaaa");
        hostHolder.setUser(sysUser);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
