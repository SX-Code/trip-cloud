package com.swx.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.domain.StrategyTheme;
import com.swx.article.mapper.StrategyCatalogMapper;
import com.swx.article.mapper.StrategyThemeMapper;
import com.swx.article.service.StrategyCatalogService;
import com.swx.article.service.StrategyThemeService;
import org.springframework.stereotype.Service;

@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog> implements StrategyCatalogService {
}
