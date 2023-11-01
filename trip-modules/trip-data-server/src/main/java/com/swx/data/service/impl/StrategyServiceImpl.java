package com.swx.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.Strategy;
import com.swx.data.mapper.StrategyMapper;
import com.swx.data.service.StrategyService;
import org.springframework.stereotype.Service;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
}
