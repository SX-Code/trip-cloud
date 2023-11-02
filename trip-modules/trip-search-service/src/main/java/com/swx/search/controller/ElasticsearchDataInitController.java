package com.swx.search.controller;

import com.alibaba.fastjson2.JSON;
import com.swx.article.dto.DestinationDTO;
import com.swx.article.dto.StrategyDTO;
import com.swx.article.dto.TravelDTO;
import com.swx.common.core.exception.BizException;
import com.swx.common.core.qo.QueryObject;
import com.swx.common.core.utils.R;
import com.swx.common.redis.service.RedisService;
import com.swx.search.DestinationEs;
import com.swx.search.StrategyEs;
import com.swx.search.TravelEs;
import com.swx.search.UserInfoEs;
import com.swx.search.feign.ArticleFeignService;
import com.swx.search.feign.UserInfoFeignService;
import com.swx.search.service.ElasticsearchService;
import com.swx.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/init")
@RefreshScope
public class ElasticsearchDataInitController {

    public static final String INIT_USER = "user";
    public static final String INIT_TRAVEL = "travel";
    public static final String INIT_STRATEGY = "strategy";
    public static final String INIT_DESTINATION = "destination";
    public static final Integer BATCH_COUNT = 200;
    private final Map<String, EsDataInitStrategy> DATA_HANDLER_STRATEGY_MAP = new HashMap<>();

    @Value("${es.init.key}")
    private String initKey;
    private final RedisService redisService;
    private final UserInfoFeignService userInfoFeignService;
    private final ArticleFeignService articleFeignService;
    private final ElasticsearchService elasticsearchService;

    public ElasticsearchDataInitController(RedisService redisService, UserInfoFeignService userInfoFeignService, ArticleFeignService articleFeignService, ElasticsearchService elasticsearchService) {
        this.redisService = redisService;
        this.userInfoFeignService = userInfoFeignService;
        this.articleFeignService = articleFeignService;
        this.elasticsearchService = elasticsearchService;
    }

    @Getter
    @Setter
    static class EsDataInitStrategy {
        private Function<QueryObject, R<List<Object>>> function;
        private Class<?> clazz;

        public EsDataInitStrategy(Function<QueryObject, R<List<Object>>> function, Class<?> clazz) {
            this.function = function;
            this.clazz = clazz;
        }
    }

    @PostConstruct
    public void postConstruct() {
        // 用户初始化
        EsDataInitStrategy userInit = new EsDataInitStrategy((qo) -> userInfoFeignService.findList(qo.getCurrent(), qo.getSize()), UserInfoEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_USER, userInit);
        // 文章初始化
        EsDataInitStrategy strategyInit = new EsDataInitStrategy(articleFeignService::strategySearch, StrategyEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_STRATEGY, strategyInit);

        EsDataInitStrategy travelInit = new EsDataInitStrategy(articleFeignService::travelSearch, TravelEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_TRAVEL, travelInit);

        EsDataInitStrategy destinationInit = new EsDataInitStrategy(articleFeignService::destinationSearch, DestinationEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_DESTINATION, destinationInit);
    }

    @GetMapping("/{key}/{type}")
    public ResponseEntity<?> init(@PathVariable("key") String key, @PathVariable("type") String type) {
        log.info("[ES 数据初始化] -------------------- 数据初始化开始 --------------------");
        if (StringUtils.isEmpty(key) || !initKey.equals(key)) {
            log.warn("[ES 数据初始化] 非法操作，请求参数有误 key={}, type={}, initKey={}", key, type, initKey);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 用户访问过，就不允许再访问了
        String redisKey = "es:init:" + key + type;
        Boolean ret = redisService.setnx(redisKey, "initialized");
        if (ret == null || !ret) {
            log.warn("[ES 数据初始化] 非法操作，已初始化过, redisKey={}, ret={}", redisKey, ret);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 开始初始化数据
        this.doInit(type);
        log.info("[ES 数据初始化] -------------------- 数据初始化完成 --------------------");
        return ResponseEntity.ok().body("init success");
    }

    private void doInit(String type) {
        int current = 1;
        do {
            List<Object> list = handleRemoteDataList(current++, type);
            if (list == null || list.isEmpty()) {
                log.info("[ES 数据初始化] 数据初始化完成.");
                return;
            }
            elasticsearchService.save(list);
        } while (true);

    }

    /**
     * 获取并处理远程数据
     */
    private List<Object> handleRemoteDataList(Integer current, String type) {
        EsDataInitStrategy strategy = DATA_HANDLER_STRATEGY_MAP.get(type);
        if (strategy == null) {
            throw new BizException("初始化参数类型错误");
        }
        R<List<Object>> ret = strategy.getFunction().apply(new QueryObject(current, BATCH_COUNT));
        log.info("[ES 数据初始化] 初始化开始，查询{}数据 data={}", type, JSON.toJSONString(ret));
        List<Object> list = ret.checkAndGet();
        if (list == null || list.isEmpty()) {
            return list;
        }
        List<Object> dataList = new ArrayList<>(list.size());
        try {
            Class<?> clazz = strategy.getClazz();
            for (Object dto : list) {
                Object es = clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(es, dto);
                dataList.add(es);
            }
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
