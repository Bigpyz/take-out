package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.dto.UserRegisterDTO;
import com.sky.dto.UserUpdateDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import com.sky.vo.UserProfileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * C端用户登录/注册
 */
@RestController
@RequestMapping("/user/user")
@Api(tags = "C端用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation("用户登录（用户名或手机号+密码）")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);
        User user = userService.login(userLoginDTO);

        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.success();
    }

    @GetMapping("/profile")
    @ApiOperation("获取当前用户信息")
    public Result<UserProfileVO> profile() {
        Long userId = com.sky.context.BaseContext.getCurrentId();
        UserProfileVO vo = userService.getCurrentUserProfile(userId);
        return Result.success(vo);
    }

    @PutMapping("/profile")
    @ApiOperation("修改当前用户信息")
    public Result updateProfile(@RequestBody UserUpdateDTO dto) {
        Long userId = com.sky.context.BaseContext.getCurrentId();
        userService.updateCurrentUserProfile(userId, dto);
        return Result.success();
    }

}
