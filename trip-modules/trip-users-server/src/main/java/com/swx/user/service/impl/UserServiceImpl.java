package com.swx.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.common.core.exception.BizException;
import com.swx.common.core.utils.Md5Utils;
import com.swx.common.core.utils.R;
import com.swx.common.redis.service.RedisService;
import com.swx.common.security.service.TokenService;
import com.swx.common.security.vo.LoginUser;
import com.swx.user.domain.UserInfo;
import com.swx.user.mapper.UserInfoMapper;
import com.swx.user.redis.key.UserRedisKeyPrefix;
import com.swx.user.service.UserInfoService;
import com.swx.user.vo.LoginUserVo;
import com.swx.user.vo.RegisterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final RedisService redisService;
    private final TokenService tokenService;

    public UserServiceImpl(RedisService redisService, TokenService tokenService) {
        this.redisService = redisService;
        this.tokenService = tokenService;
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
     * 登陆接口
     *
     * @param username 用户名
     * @param password 密码
     * @return {token, 用户}
     */
    @Override
    public Map<String, Object> login(String username, String password) {
        // 1. 基于用户名查询用户对象
        UserInfo userInfo = this.findByPhone(username);
        if (userInfo == null) {
            throw new BizException(500401, "用户名或密码错误");
        }
        // 2. 对参数密码进行加密
        String encryptPassword = Md5Utils.getMD5(password + username);
        // 3. 校验前端密码和数据库密码是否一致
        if (!encryptPassword.equalsIgnoreCase(userInfo.getPassword())) {
            throw new BizException(500401, "用户名或密码错误");
        }

        // 根据用户信息生成 jwt token
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(userInfo, loginUser);
        String jwtToken = tokenService.createToken(loginUser);

        // 构建 Map 对象，存入Token 和用户对象，返回
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(userInfo, loginUserVo);
        Map<String, Object> data = new HashMap<>();
        data.put("token", jwtToken);
        data.put("user", loginUserVo);
        return data;
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
