package com.swx.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.user.domain.UserInfo;
import com.swx.user.dto.UserInfoDTO;
import com.swx.user.vo.RegisterRequest;

import java.util.List;
import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    /**
     * 基于手机号查询用户对象
     *
     * @param phone 手机号
     * @return 用户对象
     */
    UserInfo findByPhone(String phone);

    /**
     * 注册接口
     *
     * @param req 注册请求对象
     */
    void register(RegisterRequest req);

    /**
     * 登陆接口
     *
     * @param username 用户名
     * @param password 密码
     * @return {token, 用户}
     */
    Map<String, Object> login(String username, String password);

    UserInfoDTO getDtoById(Long id);

    /**
     * 获取用户收藏攻略
     *
     * @param userId 用户ID
     * @return 收藏攻略
     */
    List<Long> getFavorStrategyIdList(Long userId);

    /**
     * 收藏攻略
     *
     * @param sid 攻略ID
     * @return 收藏状态
     */
    Boolean favoriteStrategy(Long sid);
}
