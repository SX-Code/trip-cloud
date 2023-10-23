package com.swx.common.redis.key;

import java.util.concurrent.TimeUnit;

/**
 * Redis key 通用规范设计
 */
public interface KeyPrefix {

    /**
     * @return redis key 前缀
     */
    String getPrefix();

    /**
     * @return 超时时间, -1 表示没有超时时间
     */
    default Long getTimeout() {
        return -1L;
    }

    /**
     * @return 超时时间, 如果有超时时间，必须要有单位，没有可不设置
     */
    default TimeUnit getUnit() {
        return null;
    }

    /**
     * 拼接完整的key
     * @param suffix 待拼接
     * @return 完整的key
     */
    default String fullKey(String... suffix) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(getPrefix());
        for (String s : suffix) {
            sb.append(":").append(s);
        }
        return sb.toString();
    }
}
