package com.swx.article.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Travel;
import com.swx.article.qo.TravelQuery;
import com.swx.article.service.TravelService;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/travels")
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/query")
    public R<Page<Travel>> pageList(TravelQuery query) {
        return R.ok(travelService.pageList(query));
    }

    @GetMapping("/detail")
    public R<Travel> getById(Long id) {
        return R.ok(travelService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(Travel travel) {
        travelService.save(travel);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> update(Travel travel) {
        travelService.updateById(travel);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable Long id) {
        travelService.removeById(id);
        return R.ok();
    }

}