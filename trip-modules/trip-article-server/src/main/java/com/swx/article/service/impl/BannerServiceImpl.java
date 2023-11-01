package com.swx.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.Banner;
import com.swx.article.mapper.BannerMapper;
import com.swx.article.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
    @Override
    public List<Banner> findByType(Integer type) {
        return list(Wrappers.<Banner>lambdaQuery()
                .eq(Banner::getType, type)
                .eq(Banner::getState, Banner.STATE_NORMAL)
                .orderByAsc(Banner::getSeq)
        );
    }
}
