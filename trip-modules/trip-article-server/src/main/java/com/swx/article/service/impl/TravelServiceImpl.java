package com.swx.article.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.Travel;
import com.swx.article.feign.UserInfoFeignService;
import com.swx.article.mapper.TravelMapper;
import com.swx.article.qo.TravelQuery;
import com.swx.article.service.TravelService;
import com.swx.article.vo.TravelRange;
import com.swx.common.core.utils.R;
import com.swx.user.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {

    private final UserInfoFeignService userInfoFeignService;
    private final ThreadPoolExecutor bizThreadPoolExecutor;

    public TravelServiceImpl(UserInfoFeignService userInfoFeignService, ThreadPoolExecutor bizThreadPoolExecutor) {
        this.userInfoFeignService = userInfoFeignService;
        this.bizThreadPoolExecutor = bizThreadPoolExecutor;
    }

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
        Page<Travel> page = super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
        List<Travel> travels = page.getRecords();

        // 计数器，等待
        CountDownLatch latch = new CountDownLatch(travels.size());
        for (Travel travel : travels) {
            // 线程池，多线程执行
            bizThreadPoolExecutor.execute(() -> {
                try {
                    R<UserInfoDTO> result = userInfoFeignService.getById(travel.getAuthorId());
                    if (result.getCode() != R.CODE_SUCCESS) {
                        log.warn("[游记服务] 查询用户作者失败，返回数据异常: {}", JSON.toJSONString(result));
                        return;
                    }
                    travel.setAuthor(result.getData());
                } finally {
                    // 倒计时数量-1
                    latch.countDown();
                }

            });

        }
        // 返回结果前阻塞等待
        try {
            latch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return page;
    }
}
