package com.swx.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.domain.StrategyTheme;
import com.swx.article.vo.StrategyCatalogGroup;

import java.util.List;

public interface StrategyCatalogService extends IService<StrategyCatalog> {
    /**
     * 根据目的地分组查询类别
     * @return 所有目的地类别分组
     */
    List<StrategyCatalogGroup> findGroupList();
}
