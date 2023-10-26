package com.swx.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swx.article.domain.StrategyCatalog;
import com.swx.article.vo.StrategyCatalogGroup;

import java.util.List;

public interface StrategyCatalogMapper extends BaseMapper<StrategyCatalog> {
    List<StrategyCatalogGroup> selectGroupList();
}
