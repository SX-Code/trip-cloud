package com.swx.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.Region;
import com.swx.article.mapper.RegionMapper;
import com.swx.article.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    /**
     * 热门区域查询
     *
     * @return 热门区域
     */
    @Override
    public List<Region> findHotList() {
        return list(Wrappers.<Region>lambdaQuery()
                .eq(Region::getIshot, Region.STATE_HOT)
                .orderByAsc(Region::getSeq));
    }
}
