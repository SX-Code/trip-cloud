package com.swx.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Strategy;
import com.swx.article.domain.StrategyTheme;
import com.swx.article.service.StrategyThemeService;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/strategies/themes")
public class StrategyThemeController {

    private final StrategyThemeService strategyThemeService;

    public StrategyThemeController(StrategyThemeService strategyThemeService) {
        this.strategyThemeService = strategyThemeService;
    }

    @GetMapping("/query")
    public R<Page<StrategyTheme>> pageList(Page<StrategyTheme> page) {
        return R.ok(strategyThemeService.page(page));
    }

    @GetMapping("/detail")
    public R<StrategyTheme> getById(Long id) {
        return R.ok(strategyThemeService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(StrategyTheme strategyTheme) {
        strategyThemeService.save(strategyTheme);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> update(StrategyTheme strategyTheme) {
        strategyThemeService.updateById(strategyTheme);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable Long id) {
        strategyThemeService.removeById(id);
        return R.ok();
    }
}
