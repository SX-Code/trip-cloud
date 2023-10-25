package com.swx.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.article.domain.Destination;
import com.swx.article.qo.DestinationQuery;

import java.util.List;

public interface DestinationService extends IService<Destination> {
    /**
     * 根据区域ID获取目的地
     *
     * @param regionId 区域ID
     */
    List<Destination> getDestinationByRegionId(Long regionId);

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 分页数据
     */
    Page<Destination> pageList(DestinationQuery query);

    /**
     * 根据当前ID查询面包屑
     * @param destId 当前目的地ID
     * @return 面包屑
     */
    List<Destination> toasts(Long destId);

    /**
     * 根据热门区域ID查询热门目的地
     * @param rid 区域ID
     * @return 热门目的地
     */
    List<Destination> findHotList(Long rid);
}
