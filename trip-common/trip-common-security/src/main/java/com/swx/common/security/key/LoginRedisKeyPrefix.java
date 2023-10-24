package com.swx.common.security.key;

import com.swx.common.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class LoginRedisKeyPrefix extends BaseKeyPrefix {
    public static final LoginRedisKeyPrefix USER_LOGIN_INFO_STRING = new LoginRedisKeyPrefix("USER:LOGIN:INFO");

    public LoginRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public LoginRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
