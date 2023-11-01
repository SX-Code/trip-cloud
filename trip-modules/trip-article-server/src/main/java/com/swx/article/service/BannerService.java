package com.swx.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.Banner;

import java.util.List;

public interface BannerService extends IService<Banner> {

    public List<Banner> findByType(Integer type);
}
