package com.swx.article.qo;

import com.swx.common.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrategyQuery extends QueryObject {
    public static final int CONDITION_THEME = 3;

    private Long destId; // 目的地ID
    private Long themeId; // 主题ID
    private Long refid; // 筛选条件ID => 目的地ID｜主题ID
    private Integer type; // 筛选条件类型 => 目的地｜主题
    private String orderBy;
}
