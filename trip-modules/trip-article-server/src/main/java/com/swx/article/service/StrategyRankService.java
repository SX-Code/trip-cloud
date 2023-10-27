package com.swx.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.StrategyRank;

import java.util.List;

public interface StrategyRankService extends IService<StrategyRank> {
    /**
     * 根据类型获取排名
     *
     * @param type 类型
     * @return 排名
     */
    List<StrategyRank> selectLastRanksByType(int type);
}
