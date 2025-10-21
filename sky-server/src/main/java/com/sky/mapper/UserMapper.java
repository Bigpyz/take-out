package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    User getById(String id);

    /**
     * 根据openid获取当前用户（已弃用）
     */
    User getByOpenId(@Param("openid") String openid);

    /**
     * 创建新用户
     * @param user
     */
    void insert(User user);

    /**
     * 更新用户（按主键可选字段更新）
     */
    void update(User user);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 根据用户名获取用户
     */
    User getByUsername(@Param("username") String username);

    /**
     * 根据手机号获取用户
     */
    User getByPhone(@Param("phone") String phone);
}
