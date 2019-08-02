package com.xsy.documentduplicatechecking.helper;

import com.xsy.documentduplicatechecking.bo.SysUserBo;
import com.xsy.documentduplicatechecking.entity.SysUser;
import com.xsy.documentduplicatechecking.request.RegRequest;
import org.springframework.beans.BeanUtils;

/**
 * @author xushiyue
 * @date 2019年7月30日15:34:18
 */
public class SysUserHelper {

    public static SysUser convertToSysUser(SysUserBo sysUserBo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserBo, sysUser);
        return sysUser;
    }

    public static SysUserBo convertToSysUserBo(RegRequest regRequest) {
        SysUserBo sysUserBo = new SysUserBo();
        BeanUtils.copyProperties(regRequest, sysUserBo);
        return sysUserBo;
    }
}
