package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.dto.UserUpdateDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import com.sky.vo.UserProfileVO;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        String password = userLoginDTO.getPassword();

        if (!StringUtils.hasText(account) || !StringUtils.hasText(password)) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 允许用户名或手机号登录
        User user = userMapper.getByUsername(account);
        if (user == null) {
            user = userMapper.getByPhone(account);
        }
        if (user == null) {
            throw new LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        String encrypted = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encrypted.equals(user.getPassword())) {
            throw new LoginFailedException(MessageConstant.PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        // 校验唯一
        if (userMapper.getByUsername(userRegisterDTO.getUsername()) != null) {
            throw new LoginFailedException("用户名已存在");
        }
        if (StringUtils.hasText(userRegisterDTO.getPhone()) && userMapper.getByPhone(userRegisterDTO.getPhone()) != null) {
            throw new LoginFailedException("手机号已存在");
        }

        User user = User.builder()
                .username(userRegisterDTO.getUsername())
                .phone(userRegisterDTO.getPhone())
                .password(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()))
                .createTime(LocalDateTime.now())
                .build();
        userMapper.insert(user);
    }

    @Override
    public UserProfileVO getCurrentUserProfile(Long userId) {
        User user = userMapper.getById(String.valueOf(userId));
        if (user == null) {
            return null;
        }
        return UserProfileVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .sex(user.getSex())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public void updateCurrentUserProfile(Long userId, UserUpdateDTO dto) {
        User toUpdate = User.builder()
                .id(userId)
                .name(dto.getName())
                .phone(dto.getPhone())
                .sex(dto.getSex())
                .avatar(dto.getAvatar())
                .build();
        userMapper.update(toUpdate);
    }
}
