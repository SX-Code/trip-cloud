package com.swx.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.Region;

import java.util.List;

public interface RegionService extends IService<Region> {
    /**
     * 热门区域查询
     * @return 热门区域
     */
    List<Region> findHotList();
}
