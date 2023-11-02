package com.swx.article.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.article.domain.Destination;
import com.swx.article.domain.Region;
import com.swx.article.dto.DestinationDTO;
import com.swx.article.qo.DestinationQuery;
import com.swx.article.service.DestinationService;
import com.swx.common.core.qo.QueryObject;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public R<Page<Destination>> pageList(DestinationQuery query) {
        return R.ok(destinationService.pageList(query));
    }

    @GetMapping("/list")
    public R<List<Destination>> listAll() {
        return R.ok(destinationService.list());
    }

    @PostMapping("/search")
    public R<List<Destination>> forSearchService(@RequestBody QueryObject qo) {
        return R.ok(destinationService.list(Wrappers.<Destination>query().last("limit " + qo.getOffset() + ", " + qo.getSize())));
    }

    @GetMapping("/detail")
    public R<Destination> getById(Long id) {
        return R.ok(destinationService.getById(id));
    }

    @GetMapping("/hotList")
    public R<List<Destination>> hotList(Long rid) {
        return R.ok(destinationService.findHotList(rid));
    }

    @PostMapping("/save")
    public R<?> save(Destination dst) {
        destinationService.save(dst);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> update(Destination dst) {
        destinationService.updateById(dst);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> delete(@PathVariable Long id) {
        destinationService.removeById(id);
        return R.ok();
    }

    @GetMapping("/toasts")
    public R<List<Destination>> toasts(Long destId) {
        return R.ok(destinationService.toasts(destId));
    }
}
