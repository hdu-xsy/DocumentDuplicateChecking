package com.xsy.documentduplicatechecking.entity;

import org.springframework.stereotype.Component;

/**
 * @author xushiyue
 * @date 2019年7月30日17:15:37
 */
@Component
public class HostHolder {

    private static ThreadLocal<SysUser> users = new ThreadLocal<>();

    public SysUser getUser() {
        return users.get();
    }

    public void setUser(SysUser user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
