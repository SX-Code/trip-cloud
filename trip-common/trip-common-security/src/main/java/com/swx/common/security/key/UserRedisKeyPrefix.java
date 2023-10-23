package com.swx.common.security.key;

import com.swx.common.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class UserRedisKeyPrefix extends BaseKeyPrefix {
    public static final UserRedisKeyPrefix USER_LOGIN_INFO_STRING = new UserRedisKeyPrefix("USER:LOGIN:INFO");

    public UserRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public UserRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
