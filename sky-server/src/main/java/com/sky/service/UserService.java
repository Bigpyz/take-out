package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.dto.UserUpdateDTO;
import com.sky.entity.User;
import com.sky.vo.UserProfileVO;

public interface UserService {
    /**
     * 用户名或手机号密码登录
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * 用户注册
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 查询当前登录用户资料
     */
    UserProfileVO getCurrentUserProfile(Long userId);

    /**
     * 更新当前登录用户资料
     */
    void updateCurrentUserProfile(Long userId, UserUpdateDTO dto);
}
