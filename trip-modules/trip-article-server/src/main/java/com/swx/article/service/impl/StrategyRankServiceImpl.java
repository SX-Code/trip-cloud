package com.swx.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.StrategyRank;
import com.swx.article.mapper.StrategyRankMapper;
import com.swx.article.service.StrategyRankService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyRankServiceImpl extends ServiceImpl<StrategyRankMapper, StrategyRank> implements StrategyRankService {

    @Override
    public List<StrategyRank> selectLastRanksByType(int type) {
        return list(Wrappers.<StrategyRank>lambdaQuery()
                .eq(StrategyRank::getType, type)
                .orderByDesc(StrategyRank::getStatisTime)
                .last("limit 10")
        );
    }
}
