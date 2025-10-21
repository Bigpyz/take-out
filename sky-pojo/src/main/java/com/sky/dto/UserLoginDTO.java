package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户账号密码登录
 */
@Data
public class UserLoginDTO implements Serializable {

    // 支持用户名或手机号
    private String account;

    private String password;

}
