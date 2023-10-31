package com.swx.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.*;
import com.swx.article.feign.UserInfoFeignService;
import com.swx.article.mapper.StrategyContentMapper;
import com.swx.article.mapper.StrategyMapper;
import com.swx.article.qo.StrategyQuery;
import com.swx.article.redis.key.StrategyRedisKeyPrefix;
import com.swx.article.service.DestinationService;
import com.swx.article.service.StrategyCatalogService;
import com.swx.article.service.StrategyService;
import com.swx.article.service.StrategyThemeService;
import com.swx.article.utils.OssUtil;
import com.swx.article.vo.StrategyCondition;
import com.swx.common.core.exception.BizException;
import com.swx.common.core.utils.DateUtils;
import com.swx.common.core.utils.R;
import com.swx.common.redis.service.RedisService;
import com.swx.common.security.util.AuthenticationUtil;
import com.swx.common.security.vo.LoginUser;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {

    private final StrategyCatalogService strategyCatalogService;
    private final DestinationService destinationService;
    private final StrategyThemeService strategyThemeService;
    private final StrategyContentMapper strategyContentMapper;
    private final RedisService redisService;
    private final UserInfoFeignService userInfoFeignService;

    public StrategyServiceImpl(StrategyCatalogService strategyCatalogService, DestinationService destinationService,
                               StrategyThemeService strategyThemeService, StrategyContentMapper strategyContentMapper,
                               RedisService redisService, @Lazy UserInfoFeignService userInfoFeignService) {
        this.strategyCatalogService = strategyCatalogService;
        this.destinationService = destinationService;
        this.strategyThemeService = strategyThemeService;
        this.strategyContentMapper = strategyContentMapper;
        this.redisService = redisService;
        this.userInfoFeignService = userInfoFeignService;
    }

    @Override
    public Strategy getById(Serializable id) {
        Strategy strategy = super.getById(id);
        StrategyContent strategyContent = strategyContentMapper.selectById(id);
        strategy.setContent(strategyContent);
        // 查询当前用户是否已收藏攻略
        LoginUser loginUser = AuthenticationUtil.getLoginUser();
        if (loginUser != null) {
            R<List<Long>> favoriteStrategyIdList = userInfoFeignService.getFavorStrategyIdList(loginUser.getId());
            List<Long> ids = favoriteStrategyIdList.checkAndGet();
            strategy.setFavorite(ids.contains(id));
        }
        // 从 redis 中查询最新的统计数据
        Map<String, Object> statData = redisService.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
        if (statData != null) {
            strategy.setViewnum((Integer) statData.get("viewnum"));
            strategy.setReplynum((Integer) statData.get("replynum"));
            strategy.setFavornum((Integer) statData.get("favornum"));
            strategy.setSharenum((Integer) statData.get("sharenum"));
            strategy.setThumbsupnum((Integer) statData.get("thumbsupnum"));
        }
        return strategy;
    }

    /**
     * 保存攻略
     *
     * @param strategy 攻略参数对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Strategy strategy) {
        // 同一类，事务调用非事务，事务都生效
        return doSaveOrUpdate(strategy);
    }

    /**
     * 更新攻略
     *
     * @param strategy 攻略参数对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(Strategy strategy) {
        return doSaveOrUpdate(strategy);
    }

    private Boolean doSaveOrUpdate(Strategy strategy) {
        // 有封面数据且是base64时才做上传处理
        if (StringUtils.hasText(strategy.getCoverUrl()) && !strategy.getCoverUrl().startsWith("http")) {
            // 上传封面图片，得到URL后，重新设置到cover属性中
            String fiilename = UUID.randomUUID().toString();
            // 可以解析base64得到格式，这里直接写死
            String url = OssUtil.uploadImgByBase64("images/strategies", fiilename + ".jpg", strategy.getCoverUrl());
            strategy.setCoverUrl(url);

        }
        // 补充分类名称
        StrategyCatalog catalog = strategyCatalogService.getById(strategy.getCatalogId());
        if (catalog == null) {
            throw new BizException(R.CODE_ERROR_PARAM, "分类参数异常");
        }
        strategy.setCatalogName(catalog.getName());
        // 根据分类目的地id/名称，设置到目的地中
        strategy.setDestId(catalog.getDestId());
        strategy.setDestName(catalog.getDestName());
        // 判断目的地是否是国外
        List<Destination> toasts = destinationService.toasts(catalog.getDestId());
        if (toasts.get(0).getId() == 1) {
            strategy.setIsabroad(Strategy.ABROAD_NO);
        } else {
            strategy.setIsabroad(Strategy.ABROAD_YES);
        }
        // 查询主题，填充主题名称
        StrategyTheme theme = strategyThemeService.getById(strategy.getThemeId());
        if (theme == null) {
            throw new BizException(R.CODE_ERROR_PARAM, "主题参数异常");
        }
        strategy.setThemeName(theme.getName());

        if (strategy.getId() == null) {
            // 设置创建时间
            strategy.setCreateTime(new Date());
            // 设置各种数量为0
            strategy.setViewnum(0);
            strategy.setSharenum(0);
            strategy.setThumbsupnum(0);
            strategy.setReplynum(0);
            strategy.setFavornum(0);
            // 重新设置状态
            strategy.setState(Strategy.STATE_NORMAL);
            // 保存攻略对象，得到自增ID
            boolean save = super.save(strategy);
            // 将攻略ID设置到内容对象中，保存内容对象
            StrategyContent content = strategy.getContent();
            content.setId(strategy.getId());
            return save && strategyContentMapper.insert(content) > 0;
        }
        // 更新操作
        boolean ret = super.updateById(strategy);
        StrategyContent content = strategy.getContent();
        content.setId(strategy.getId());
        int row = strategyContentMapper.updateById(content);
        return ret && row > 0;
    }

    /**
     * 根据目的地ID，分组查询攻略分类下的攻略
     *
     * @param destId 目的地ID
     * @return 分类及其下的攻略
     */
    @Override
    public List<StrategyCatalog> findGroupsByDestId(Long destId) {
        return baseMapper.selectGroupsByDestId(destId);
    }

    /**
     * 根据攻略ID，返回攻略内容
     *
     * @param id 攻略ID
     * @return 攻略内容
     */
    @Override
    public StrategyContent getContentById(Long id) {
        return strategyContentMapper.selectById(id);
    }

    /**
     * 根据目的地ID，查询浏览量最高的前3篇攻略
     *
     * @param destId 目的地
     * @return 浏览量最高的前3篇攻略
     */
    @Override
    public List<Strategy> findViewnumTop3(Long destId) {
        return super.list(Wrappers.<Strategy>lambdaQuery()
                .eq(Strategy::getDestId, destId)
                .orderByDesc(Strategy::getViewnum)
                .last("limit 3")
        );
    }

    /**
     * 条件分页查询攻略
     *
     * @param query 查询条件
     * @return 攻略
     */
    @Override
    public Page<Strategy> pageStrategy(StrategyQuery query) {
        // 兼容标签查询
        if ((query.getType() != null && query.getType() != -1) && (query.getRefid() != null && query.getRefid() != -1)) {
            // 多条件标签筛选，目的地或者主题查询
            if (query.getType() == StrategyQuery.CONDITION_THEME) {
                query.setThemeId(query.getRefid());
            } else {
                query.setDestId(query.getRefid());
            }
        }

        // 目的地和主题筛选
        return super.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.<Strategy>lambdaQuery()
                        .eq(query.getDestId() != null, Strategy::getDestId, query.getDestId())
                        .eq(!Objects.isNull(query.getThemeId()), Strategy::getThemeId, query.getThemeId())
                        .orderByDesc(query.getOrderBy() != null && query.getOrderBy().equals("viewnum"), Strategy::getViewnum)
                        .orderByDesc(query.getOrderBy() != null && query.getOrderBy().equals("create_time"), Strategy::getCreateTime)

        );
    }

    /**
     * 查询目的地过滤条件
     *
     * @param abroad 是否国内
     * @return 过滤条件
     */
    @Override
    public List<StrategyCondition> findDestCondition(int abroad) {
        return getBaseMapper().selectDestCondition(abroad);
    }

    /**
     * 查询主题过滤条件
     *
     * @return 过滤条件
     */
    @Override
    public List<StrategyCondition> findThemeCondition() {
        return getBaseMapper().selectThemeCondition();
    }

    /**
     * 增加阅读量
     *
     * @param id 攻略id
     */
    @Override
    public void viewnumIncr(Long id) {
        redisService.hashIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "viewnum", 1, id + "");
    }

    /**
     * 攻略置顶，一个用户一天只能置顶一篇攻略
     *
     * @param sid 攻略ID
     * @return 置顶状态
     */
    @Override
    public Boolean thumbnumIncr(Long sid) {
        LoginUser loginUser = AuthenticationUtil.getLoginUser();
        StrategyRedisKeyPrefix keyPrefix = StrategyRedisKeyPrefix.STRATEGIES_TOP_MAP;
        String key = keyPrefix.fullKey(sid + "");
        String hashKey = loginUser.getId() + "";
        // TODO: 并发问题，查询和增加不是原子操作，锁 或者 lua 脚本
        Integer count = redisService.getCacheMapValue(key, hashKey);
        if (count != null && count > 0) {
            return false;
        }
        // 记录用户的置顶，向攻略map中添加该用户的置顶，并设置攻略置顶map的过期时间
        keyPrefix.setTimeout(DateUtils.getLastMillisSeconds());
        keyPrefix.setUnit(TimeUnit.MILLISECONDS);
        redisService.hashIncrement(keyPrefix, hashKey, 1, sid + "");
        // 置顶数+1
        redisService.hashIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "thumbsupnum", 1, sid + "");
        return true;
    }

    /**
     * 获取攻略统计数据
     *
     * @param sid 攻略ID
     * @return 统计数据
     */
    @Override
    public Map<String, Object> getStatData(Long id) {
        // 从 redis 中查询最新的统计数据
        return redisService.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
    }
}
