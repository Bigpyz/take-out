package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户注册
 */
@Data
public class UserRegisterDTO implements Serializable {

    private String username; // 唯一用户名

    private String phone;    // 手机号

    private String password; // 明文提交，后端加密存储
}


