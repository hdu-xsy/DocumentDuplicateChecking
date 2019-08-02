package com.xsy.documentduplicatechecking.repository;

import com.xsy.documentduplicatechecking.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xushiyue
 * @date 2019年7月30日16:00:09
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    /**
     * 根据手机号查找.
     *
     * @param mobileNo:
     * @return sysUser
     */
    SysUser findByMobileNo(String mobileNo);

    /**
     * 根据邮箱查找.
     *
     * @param email:
     * @return sysUser
     */
    SysUser findByEmail(String email);

    /**
     * 根据用户名查找
     *
     * @param username:
     * @return sysUser
     */
    SysUser findByUsername(String username);

}
