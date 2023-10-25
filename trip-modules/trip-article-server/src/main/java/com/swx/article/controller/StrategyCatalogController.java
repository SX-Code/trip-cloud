package com.swx.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.service.StrategyCatalogService;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/strategies/catalogs")
public class StrategyCatalogController {

    private final StrategyCatalogService strategyCatalogService;

    public StrategyCatalogController(StrategyCatalogService strategyCatalogService) {
        this.strategyCatalogService = strategyCatalogService;
    }

    @GetMapping("/query")
    public R<Page<StrategyCatalog>> pageList(Page<StrategyCatalog> page) {
        return R.ok(strategyCatalogService.page(page));
    }

    @PostMapping("/save")
    public R<?> save(StrategyCatalog strategyCatalog) {
        strategyCatalogService.save(strategyCatalog);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> update(StrategyCatalog strategyCatalog) {
        strategyCatalogService.updateById(strategyCatalog);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable Long id) {
        strategyCatalogService.removeById(id);
        return R.ok();
    }
}
