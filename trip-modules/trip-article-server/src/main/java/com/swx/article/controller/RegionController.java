package com.swx.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Destination;
import com.swx.article.domain.Region;
import com.swx.article.service.DestinationService;
import com.swx.article.service.RegionService;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;
    private final DestinationService destinationService;

    public RegionController(RegionService regionService, DestinationService destinationService) {
        this.regionService = regionService;
        this.destinationService = destinationService;
    }

    @GetMapping
    public R<Page<Region>> pageList(Page<Region> page) {
        return R.ok(regionService.page(page));
    }

    @GetMapping("/detail")
    public R<Region> getById(Long id) {
        return R.ok(regionService.getById(id));
    }

    @GetMapping("/hotList")
    public R<List<Region>> hotList() {
        return R.ok(regionService.findHotList());
    }

    @PostMapping("/save")
    public R<?> save(Region region) {
        regionService.save(region);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> update(Region region) {
        regionService.updateById(region);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable Long id) {
        regionService.removeById(id);
        return R.ok();
    }

    @GetMapping("/{id}/destination")
    public R<List<Destination>> getDestination(@PathVariable Long id) {
        return R.ok(destinationService.getDestinationByRegionId(id));
    }

}
