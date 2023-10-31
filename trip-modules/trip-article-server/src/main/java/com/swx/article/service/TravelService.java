package com.swx.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.Travel;
import com.swx.article.qo.TravelQuery;

import java.util.List;

public interface TravelService extends IService<Travel> {
    /**
     * 条件分页查询游记
     *
     * @param query 分页查询参数
     * @return 游记
     */
    Page<Travel> pageList(TravelQuery query);

    /**
     * 根据目的地ID，查询浏览量最高的前3篇游记
     *
     * @param destId 目的地
     * @return 浏览量最高的前3篇游记
     */
    List<Travel> findViewnumTop3(Long destId);
}
