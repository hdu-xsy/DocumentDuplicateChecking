package com.xsy.documentduplicatechecking.entity;

import com.xsy.documentduplicatechecking.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author xushiyue
 * @date 2019年7月30日14:31:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * MD5加密后密码
     */
    private String password;

    /**
     * 撒盐
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    @Column(name = "mobile_no")
    private String mobileNo;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否禁用 1为禁用
     */
    @Column(name = "is_disable")
    private Boolean isDisabled;

    /**
     * 0-运营后台用户 1-非运营后台用户 2超级管理员 3管理人员
     */
    @Column(name = "user_type")
    private Integer userType;

}
