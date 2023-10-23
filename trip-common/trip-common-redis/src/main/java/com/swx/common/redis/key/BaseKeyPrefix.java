package com.swx.common.redis.key;

import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 基础 Redis Key 前缀
 */
@Setter
public class BaseKeyPrefix implements KeyPrefix {

    private String prefix;
    private Long timeout;
    private TimeUnit unit;

    public BaseKeyPrefix(String prefix) {
        this(prefix, -1L, null);
    }

    public BaseKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        this.prefix = prefix;
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public Long getTimeout() {
        return this.timeout;
    }

    @Override
    public TimeUnit getUnit() {
        return this.unit;
    }
}
