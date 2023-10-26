package com.swx.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.domain.StrategyTheme;
import com.swx.article.mapper.StrategyCatalogMapper;
import com.swx.article.mapper.StrategyThemeMapper;
import com.swx.article.service.StrategyCatalogService;
import com.swx.article.service.StrategyThemeService;
import com.swx.article.vo.StrategyCatalogGroup;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog> implements StrategyCatalogService {

    /**
     * 根据目的地分组查询类别
     *
     * @return 所有目的地类别分组
     */
    @Override
    public List<StrategyCatalogGroup> findGroupList() {
        return baseMapper.selectGroupList();
    }
}
