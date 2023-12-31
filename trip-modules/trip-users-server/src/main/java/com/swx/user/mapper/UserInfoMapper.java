package com.swx.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swx.user.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    List<Long> getFavorStrategyIdList(@Param("userId") Long userId);

    Boolean deleteFavorStrategy(@Param("userId") Long userId, @Param("strategyId") Long strategyId);

    Boolean insertFavorStrategy(@Param("userId") Long userId, @Param("strategyId") Long strategyId);
}
