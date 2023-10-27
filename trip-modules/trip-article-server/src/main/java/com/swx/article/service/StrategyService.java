package com.swx.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.Strategy;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.domain.StrategyContent;
import com.swx.article.qo.StrategyQuery;
import com.swx.article.vo.StrategyCondition;

import java.util.List;

public interface StrategyService extends IService<Strategy> {
    /**
     * 根据目的地ID，分组查询攻略分类下的攻略
     *
     * @param destId 目的地ID
     * @return 分类及其下的攻略
     */
    List<StrategyCatalog> findGroupsByDestId(Long destId);

    /**
     * 根据攻略ID，返回攻略内容
     *
     * @param id 攻略ID
     * @return 攻略内容
     */
    StrategyContent getContentById(Long id);

    /**
     * 根据目的地ID，查询浏览量最高的前3篇攻略
     *
     * @param destId 目的地
     * @return 浏览量最高的前3篇攻略
     */
    List<Strategy> findViewnumTop3(Long destId);

    /**
     * 条件分页查询攻略
     *
     * @param query 查询条件
     * @return 攻略
     */
    Page<Strategy> pageStrategy(StrategyQuery query);

    /**
     * 查询目的地过滤条件
     * @param abroad 是否国内
     * @return 过滤条件
     */
    List<StrategyCondition> findDestCondition(int abroad);

    /**
     * 查询主题过滤条件
     * @return 过滤条件
     */
    List<StrategyCondition> findThemeCondition();
}
