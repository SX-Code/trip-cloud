package com.swx.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.Strategy;
import com.swx.article.mapper.StrategyMapper;
import com.swx.article.service.StrategyService;
import org.springframework.stereotype.Service;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
}
