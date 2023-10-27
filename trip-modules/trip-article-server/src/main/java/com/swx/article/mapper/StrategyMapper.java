package com.swx.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swx.article.domain.Strategy;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.vo.StrategyCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StrategyMapper extends BaseMapper<Strategy> {
    List<StrategyCatalog> selectGroupsByDestId(@Param("destId") Long destId);

    List<StrategyCondition> selectDestCondition(@Param("abroad") int abroad);

    List<StrategyCondition> selectThemeCondition();
}
