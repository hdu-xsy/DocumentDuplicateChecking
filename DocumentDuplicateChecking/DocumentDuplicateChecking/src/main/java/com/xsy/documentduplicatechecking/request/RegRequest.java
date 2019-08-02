package com.xsy.documentduplicatechecking.request;

import lombok.Data;

/**
 * @author xushiyue
 * @date 2019年8月1日13:57:02
 */
@Data
public class RegRequest {

    private String mobileNo;

    private String password;

    private String nickname;

    private String code;

    private String email;
}
