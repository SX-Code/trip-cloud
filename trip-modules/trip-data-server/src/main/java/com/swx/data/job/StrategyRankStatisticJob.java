package com.swx.data.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.swx.article.domain.Strategy;
import com.swx.article.domain.StrategyRank;
import com.swx.data.mapper.StrategyMapper;
import com.swx.data.mapper.StrategyRankMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

/**
 * 攻略排行数据统计任务
 */
@Slf4j
@Service
public class StrategyRankStatisticJob {

    private final StrategyMapper strategyMapper;
    private final StrategyRankMapper strategyRankMapper;

    public StrategyRankStatisticJob(StrategyMapper strategyMapper, StrategyRankMapper strategyRankMapper) {
        this.strategyMapper = strategyMapper;
        this.strategyRankMapper = strategyRankMapper;
    }

    /**
     * 每10分钟执行统计任务
     */
    @Transactional(rollbackFor = Exception.class)
    // @Scheduled(cron = "0 */10 * * * *")
    @Scheduled(cron = "0 * * * * *")
    public void statisticRank() {
        log.info("[攻略排行统计] 排行数据统计开始 >>>>>>>>>");
        Date now = new Date();
        // 删除这一次之前的所有数据
        strategyRankMapper.delete(Wrappers.<StrategyRank>lambdaQuery().lt(StrategyRank::getStatisTime, now));
        log.info("[攻略排行统计] 删除旧的排行数据 >>>>>>>>>");
        // 统计国内
        doStatistic(now, StrategyRank.TYPE_CHINA, () -> strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_NO));

        // 统计国外
        doStatistic(now, StrategyRank.TYPE_ABROAD, () -> strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_YES));

        // 统计热门
        doStatistic(now, StrategyRank.TYPE_HOT, strategyMapper::selectStrategyRankHotList);
    }

    public void doStatistic(Date now, Integer type, Supplier<List<StrategyRank>> rankSupplier) {
        List<StrategyRank> strategyRanks = rankSupplier.get();
        log.info("[攻略排行统计] 排行数据统计：type={}, ranks={}", type, strategyRanks.size());
        for (StrategyRank strategyRank : strategyRanks) {
            strategyRank.setType(type);
            strategyRank.setStatisTime(now);
        }
        // 保存到排名表中
        strategyRankMapper.batchInsert(strategyRanks);
    }
}
