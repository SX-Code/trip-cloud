package com.swx.article.controller;

import com.swx.article.domain.Banner;
import com.swx.article.service.BannerService;
import com.swx.common.core.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banners")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/strategy")
    public R<List<Banner>> getStrategyBanners() {
        return R.ok(bannerService.findByType(Banner.TYPE_STRATEGY));
    }

    @GetMapping("/travel")
    public R<List<Banner>> getTravelBanners() {
        return R.ok(bannerService.findByType(Banner.TYPE_TRAVEL));
    }
}
