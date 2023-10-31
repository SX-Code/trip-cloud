package com.swx.article.redis.key;

import com.swx.common.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class StrategyRedisKeyPrefix extends BaseKeyPrefix {

    public static final StrategyRedisKeyPrefix STRATEGIES_STAT_DATA_MAP = new StrategyRedisKeyPrefix("STRATEGIES:STAT:DATA");

    public StrategyRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public StrategyRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
