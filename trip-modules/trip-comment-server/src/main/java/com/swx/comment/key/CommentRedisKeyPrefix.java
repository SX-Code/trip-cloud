package com.swx.comment.key;

import com.swx.common.redis.key.BaseKeyPrefix;

import java.util.concurrent.TimeUnit;

public class CommentRedisKeyPrefix extends BaseKeyPrefix {
    public static final CommentRedisKeyPrefix STRATEGIES_STAT_DATA_MAP = new CommentRedisKeyPrefix("STRATEGIES:STAT:DATA");
    public CommentRedisKeyPrefix(String prefix) {
        super(prefix);
    }

    public CommentRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        super(prefix, timeout, unit);
    }
}
