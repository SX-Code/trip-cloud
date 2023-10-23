package com.swx.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.user.domain.UserInfo;
import com.swx.user.vo.RegisterRequest;

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
     * @param req 注册请求对象
     */
    void register(RegisterRequest req);
}
