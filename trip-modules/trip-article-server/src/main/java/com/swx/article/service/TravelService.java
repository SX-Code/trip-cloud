package com.swx.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.Travel;
import com.swx.article.qo.TravelQuery;

public interface TravelService extends IService<Travel> {
    /**
     * 条件分页查询游记
     *
     * @param query 分页查询参数
     * @return 游记
     */
    Page<Travel> pageList(TravelQuery query);
}
