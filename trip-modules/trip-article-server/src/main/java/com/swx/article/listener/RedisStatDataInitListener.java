package com.swx.article.listener;

import com.swx.article.domain.Strategy;
import com.swx.article.redis.key.StrategyRedisKeyPrefix;
import com.swx.article.service.StrategyService;
import com.swx.common.redis.service.RedisService;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class RedisStatDataInitListener implements ApplicationListener<ContextRefreshedEvent> {

    private final StrategyService strategyService;
    private final RedisService redisService;

    public RedisStatDataInitListener(StrategyService strategyService, RedisService redisService) {
        this.strategyService = strategyService;
        this.redisService = redisService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        System.out.println(ctx.getClass());
        if (AnnotationConfigServletWebServerApplicationContext.class == ctx.getClass()) {
            System.out.println("-------------- 容器启动完成，执行初始化数据 --------------");
            // 查询所有攻略数据
            // TODO: 不能一次加载所有数据
            List<Strategy> strategies = strategyService.list();
            System.out.println("[攻略统计数据初始化]");
            System.out.println("攻略数：" + strategies.size());
            int count = 0;
            for (Strategy strategy : strategies) {
                String fullKey = StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(strategy.getId() + "");
                Boolean exists = redisService.hasKey(fullKey);
                if (!exists) {
                    // 不存在，将数据存入 Redis
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("viewnum", strategy.getViewnum());
                    map.put("thumbsupnum", strategy.getThumbsupnum());
                    map.put("replynum", strategy.getReplynum());
                    map.put("favornum", strategy.getFavornum());
                    map.put("sharenum", strategy.getSharenum());
                    redisService.setCacheMap(fullKey, map);
                    count++;
                }
            }
            System.out.println("初始化：" + count);
            // 遍历攻略列表，判断当前对象在 Redis 中是否存在
            System.out.println("-------------- 数据初始化完成 --------------");
        }
    }
}
