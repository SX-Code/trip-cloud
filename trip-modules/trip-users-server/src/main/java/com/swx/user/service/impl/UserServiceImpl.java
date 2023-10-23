package com.swx.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.common.core.exception.BizException;
import com.swx.common.core.utils.Md5Utils;
import com.swx.common.core.utils.R;
import com.swx.common.redis.service.RedisService;
import com.swx.user.domain.UserInfo;
import com.swx.user.mapper.UserInfoMapper;
import com.swx.user.redis.key.UserRedisKeyPrefix;
import com.swx.user.service.UserInfoService;
import com.swx.user.vo.RegisterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final RedisService redisService;

    public UserServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

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

    /**
     * 注册接口
     *
     * @param req 注册请求对象
     */
    @Override
    public void register(RegisterRequest req) {
        // 1. 基于手机号查询是否已经存在该手机号，如果存在则返回异常
        UserInfo byPhone = findByPhone(req.getPhone());
        if (byPhone != null) {
            throw new BizException(R.CODE_REGISTER_ERROR, "手机号已存在，请不要重复注册");
        }
        // 2. 从 redis 中获取验证码与前端传入的验证码进行校验是否一致，如果不一致则抛出异常
        UserRedisKeyPrefix keyPrefix = UserRedisKeyPrefix.USER_REGISTER_VERIFY_CODE_STRING;
        String code = redisService.getCacheObject(keyPrefix.fullKey(req.getPhone()));
        if (!req.getVerifyCode().equalsIgnoreCase(code)) {
            throw new BizException(R.CODE_REGISTER_ERROR, "验证码错误");
        }
        // 3. 将验证码从 redis 中删除
        redisService.deleteObject(keyPrefix.fullKey(req.getPhone()));
        // 4. 创建用户对象，填入参数并补充其他默认值
        UserInfo userInfo = this.buildUserInfo(req);
        // 5. 对密码进行加密操作，加盐 + 散列(hash)次数
        String encryptPassword = Md5Utils.getMD5(userInfo.getPassword() + userInfo.getPhone());
        userInfo.setPassword(encryptPassword);
        // 6. 保存用户对象到数据库
        super.save(userInfo);
    }

    /**
     * 构建用户对象
     * @param req 请求对象参数
     * @return 用户对象
     */
    private UserInfo buildUserInfo(RegisterRequest req) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(req, userInfo);
        userInfo.setInfo("这个人很懒，什么都没写");
        userInfo.setHeadImgUrl("/images/default.jpg");
        return userInfo;
    }
}
