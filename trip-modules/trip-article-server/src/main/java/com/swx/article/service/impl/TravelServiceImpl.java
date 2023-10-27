package com.swx.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.Travel;
import com.swx.article.mapper.TravelMapper;
import com.swx.article.qo.TravelQuery;
import com.swx.article.service.TravelService;
import com.swx.article.vo.TravelRange;
import org.springframework.stereotype.Service;

@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {
    /**
     * 条件分页查询游记
     *
     * @param query 分页查询参数
     * @return 游记
     */
    @Override
    public Page<Travel> pageList(TravelQuery query) {
        QueryWrapper<Travel> wrapper = Wrappers.<Travel>query()
                .eq(query.getDestId() != null, "dest_id", query.getDestId());
        // 旅行时间条件
        if (query.getTravelTimeRange() != null) {
            TravelRange timeRange = query.getTravelTimeRange();
            wrapper.between("MONTH(travel_time)", timeRange.getMin(), timeRange.getMax());
        }
        // 人均花费条件
        if (query.getCostRange() != null) {
            TravelRange costRange = query.getCostRange();
            wrapper.between("avg_consume", costRange.getMin(), costRange.getMax());
        }
        // 出行天数条件
        if (query.getDayRange() != null) {
            TravelRange dayRange = query.getDayRange();
            wrapper.between("day", dayRange.getMin(), dayRange.getMax());
        }
        return super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
    }
}
