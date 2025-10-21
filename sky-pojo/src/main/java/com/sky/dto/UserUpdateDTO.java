package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户资料更新
 */
@Data
public class UserUpdateDTO implements Serializable {

    private String name;
    private String phone;
    private String sex;
    private String avatar;
}


