package com.swx.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Region;
import com.swx.article.domain.Strategy;
import com.swx.article.service.StrategyService;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/strategies")
public class StrategyController {
    
    private final StrategyService strategyService;

    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    @GetMapping("/query")
    public R<Page<Strategy>> pageList(Page<Strategy> page) {
        return R.ok(strategyService.page(page));
    }

    @GetMapping("/detail")
    public R<Strategy> getById(Long id) {
        return R.ok(strategyService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(Strategy strategy) {
        strategyService.save(strategy);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> update(Strategy strategy) {
        strategyService.updateById(strategy);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable Long id) {
        strategyService.removeById(id);
        return R.ok();
    }
}
