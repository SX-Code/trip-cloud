package com.swx.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swx.article.domain.StrategyRank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyRankMapper extends BaseMapper<StrategyRank> {

    int batchInsert(@Param("strategyRanks") List<StrategyRank> strategyRanks);
}
