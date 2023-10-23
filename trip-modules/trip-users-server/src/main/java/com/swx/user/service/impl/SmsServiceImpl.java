package com.swx.user.service.impl;

import com.swx.common.redis.service.RedisService;
import com.swx.user.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private final RedisService redisService;

    public SmsServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 注册发送短信功能
     *
     * @param phone 手机号
     */
    @Override
    public void registerSmsSend(String phone) {
        // TODO 1. 验证手机合法性
        // TODO 如何实现60s限制
        // TODO 针对发送短信类的接口，是否需要进行限流？限流的频率设置多少合适？
        // 2. 生成验证码
        String code = this.generateVerifyCode("LETTER", 4);
        // 3. 缓存验证码
        redisService.setCacheObject("USERS:REGISTER:VERIFY_CODE:" + phone, code, 10L, TimeUnit.MINUTES);
        // TODO 4. 调用第三方接口，发送验证码
    }

    private String generateVerifyCode(String type, int len) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String code = uuid.substring(0, len);
        log.info("[短信服务] 生成验证码 ===== type={}, len={}, code={}", type, len, code);
        return code;
    }
}
