package com.swx.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.user.domain.UserInfo;
import com.swx.user.mapper.UserInfoMapper;
import com.swx.user.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    /**
     * 基于手机号查询用户对象
     *
     * @param phone 手机号
     * @return 用户对象
     */
    @Override
    public UserInfo findByPhone(String phone) {
        return getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getPhone, phone));
    }
}
