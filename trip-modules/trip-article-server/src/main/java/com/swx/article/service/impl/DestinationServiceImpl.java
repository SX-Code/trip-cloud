package com.swx.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.article.domain.Destination;
import com.swx.article.domain.Region;
import com.swx.article.mapper.DestinationMapper;
import com.swx.article.qo.DestinationQuery;
import com.swx.article.service.DestinationService;
import com.swx.article.service.RegionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {

    private final RegionService regionService;

    public DestinationServiceImpl(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 根据区域ID获取目的地
     *
     * @param regionId 区域ID
     */
    @Override
    public List<Destination> getDestinationByRegionId(Long regionId) {
        Region region = regionService.getById(regionId);
        if (region == null) {
            return Collections.emptyList();
        }
        List<Long> ids = region.parseRefIds();
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return listByIds(ids);
    }

    /**
     * 分页查询
     *
     * @param query 查询参数
     * @return 分页数据
     */
    @Override
    public Page<Destination> pageList(DestinationQuery query) {
        LambdaQueryWrapper<Destination> wrapper = Wrappers.<Destination>lambdaQuery();
        // parentId 为 null，查询所有 parent_id IS NULL 的数据
        wrapper.isNull(query.getParentId() == null, Destination::getParentId);
        // parentId 不为 null，根据 parent_id 查询
        wrapper.eq(query.getParentId() != null, Destination::getParentId, query.getParentId());
        // 关键字查询
        wrapper.like(StringUtils.hasText(query.getKeyword()), Destination::getName, query.getKeyword());
        return super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
    }

    /**
     * 根据当前ID查询面包屑
     *
     * @param destId 当前目的地ID
     */
    @Override
    public List<Destination> toasts(Long destId) {
        ArrayList<Destination> toasts = new ArrayList<>();
        while (destId != null) {
            Destination dest = super.getById(destId);
            if (dest == null) {
                break;
            }
            toasts.add(dest);
            destId = dest.getParentId();
        }
        Collections.reverse(toasts);
        return toasts;
    }

    /**
     * 根据热门区域ID查询热门目的地
     *
     * @param rid 区域ID
     * @return 热门目的地
     */
    @Override
    public List<Destination> findHotList(Long rid) {
        List<Destination> destinations = null;
        if (rid < 0) {
            destinations = this.baseMapper.selectHotListByRid(rid, null);
        } else {
            Region region = regionService.getById(rid);
            if (region == null) {
                return Collections.emptyList();
            }
            List<Long> ids = region.parseRefIds();
            destinations = this.baseMapper.selectHotListByRid(rid, ids);
        }
        for (Destination destination : destinations) {
            List<Destination> children = destination.getChildren();
            if (children == null) {
                continue;
            }
            destination.setChildren(children.stream().limit(10).collect(Collectors.toList()));
        }
        return destinations;
    }

    /**
     * 使用代码循环方式，有N+1问题
     *
     * @param rid 区域ID
     * @return 热门目的地
     */
    public List<Destination> findHostList2(Long rid) {
        List<Destination> destinations = null;
        LambdaQueryWrapper<Destination> wrapper = new LambdaQueryWrapper<>();
        if (rid < 0) {
            destinations = list(wrapper.eq(Destination::getParentId, 1));
        } else {
            destinations = this.getDestinationByRegionId(rid);
        }

        for (Destination destination : destinations) {
            // 清楚之前的条件
            wrapper.clear();
            List<Destination> children = list(wrapper.eq(Destination::getParentId, destination.getId()).last("limit 10"));
            destination.setChildren(children);
        }
        return destinations;
    }
}
