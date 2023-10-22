package com.swx.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.user.domain.UserInfo;
import com.swx.user.mapper.UserInfoMapper;
import com.swx.user.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
