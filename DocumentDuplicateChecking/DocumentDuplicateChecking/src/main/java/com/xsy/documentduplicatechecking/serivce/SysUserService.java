package com.xsy.documentduplicatechecking.serivce;

import com.xsy.documentduplicatechecking.bo.SysUserBo;
import com.xsy.documentduplicatechecking.exception.NotValidException;

import javax.security.auth.login.LoginException;

/**
 * @author xushiyue
 * @date 2019年7月30日15:44:53
 */
public interface SysUserService {

    /**
     * 根据token获取用户id
     *
     * @param token:
     * @return 用户id
     */
    String findByToken(String token);

    /**
     * 根据手机号码获取验证码
     *
     * @param mobileNo
     * @return 验证码
     */
    String findCodeByMobileNo(String mobileNo);

    /**
     * 清除验证过的验证码
     *
     * @param mobileNo
     */
    void removeCode(String mobileNo);

    /**
     * 将用户信息存入数据库
     *
     * @param userBo::
     * @throws NotValidException :
     */
    void saveUser(SysUserBo userBo) throws NotValidException;

    /**
     * 检查密码是否正确
     *
     * @param userName:
     * @param password:
     * @return String:
     * @exception LoginException:
     */
    String loginUser(String userName, String password) throws LoginException;
}
