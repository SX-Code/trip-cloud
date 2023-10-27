package com.swx.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swx.article.domain.Strategy;
import com.swx.article.domain.StrategyRank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {
    List<StrategyRank> selectStrategyRankByAbroad(@Param("abroad") Integer abroad);
    List<StrategyRank> selectStrategyRankHotList();
}
