package com.swx.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.StrategyTheme;
import com.swx.article.mapper.StrategyThemeMapper;
import com.swx.article.service.StrategyThemeService;
import org.springframework.stereotype.Service;

@Service
public class StrategyThemeServiceImpl extends ServiceImpl<StrategyThemeMapper, StrategyTheme> implements StrategyThemeService {
}
