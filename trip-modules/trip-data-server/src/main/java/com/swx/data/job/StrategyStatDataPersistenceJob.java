package com.swx.data.job;

import com.swx.article.domain.Strategy;
import com.swx.article.redis.key.StrategyRedisKeyPrefix;
import com.swx.common.redis.service.RedisService;
import com.swx.data.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 攻略统计数据持久化
 */
@Slf4j
@Component
public class StrategyStatDataPersistenceJob {

    private final RedisService redisService;
    private final StrategyService strategyService;

    public StrategyStatDataPersistenceJob(RedisService redisService, StrategyService strategyService) {
        this.redisService = redisService;
        this.strategyService = strategyService;
    }

    /**
     * 每10分钟执行一次
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void task() {
        log.info("[攻略数据持久化] ---------------- 持久化数据开始 ----------------");
        // 根据分数范围获取指定的成员
        Set<Integer> list = redisService.zsetRerange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
        if (list != null && !list.isEmpty()) {
            // 根据成员id，拼接key 取出统计数据
            List<Strategy> updateList = new ArrayList<>();
            for (Integer id : list) {
                Map<String, Object> map = redisService.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
                // 将数据封装为攻略对象，将对象存入待更新的集合
                Strategy strategy = new Strategy();
                strategy.setViewnum((Integer) map.get("viewnum"));
                strategy.setReplynum((Integer) map.get("replynum"));
                strategy.setFavornum((Integer) map.get("favornum"));
                strategy.setSharenum((Integer) map.get("sharenum"));
                strategy.setThumbsupnum((Integer) map.get("thumbsupnum"));
                strategy.setId(id.longValue());
                updateList.add(strategy);
            }
            // 批量更新到数据库
            strategyService.updateBatchById(updateList);
            // 删除已经更新过的成员
            redisService.zsetRemoveRange(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET, 0, Integer.MAX_VALUE);
            log.info("[攻略数据持久化] 持久化数量：{}", list.size());
        }
        log.info("[攻略数据持久化] ---------------- 持久化数据结束 ----------------");
    }
}
