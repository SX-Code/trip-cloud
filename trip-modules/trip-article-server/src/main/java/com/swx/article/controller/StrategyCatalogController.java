package com.swx.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Destination;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.service.DestinationService;
import com.swx.article.service.StrategyCatalogService;
import com.swx.article.vo.StrategyCatalogGroup;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategies/catalogs")
public class StrategyCatalogController {

    private final StrategyCatalogService strategyCatalogService;
    private final DestinationService destinationService;

    public StrategyCatalogController(StrategyCatalogService strategyCatalogService, DestinationService destinationService) {
        this.strategyCatalogService = strategyCatalogService;
        this.destinationService = destinationService;
    }

    @GetMapping("/query")
    public R<Page<StrategyCatalog>> pageList(Page<StrategyCatalog> page) {
        return R.ok(strategyCatalogService.page(page));
    }

    @GetMapping("/detail")
    public R<StrategyCatalog> getById(Long id) {
        return R.ok(strategyCatalogService.getById(id));
    }

    @GetMapping("/groups")
    public R<List<StrategyCatalogGroup>> groupList() {
        return R.ok(strategyCatalogService.findGroupList());
    }

    @PostMapping("/save")
    public R<?> save(StrategyCatalog strategyCatalog) {
        Destination destination = destinationService.getById(strategyCatalog.getDestId());
        strategyCatalog.setDestName(destination.getName());
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
