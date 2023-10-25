package com.swx.article.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("strategy_theme")
public class StrategyTheme {
    public static final int STATE_NORMAL = 1;
    public static final int STATE_DISABLE = 0;

    private Long id;
    private String name; // 主题名称
    private Integer state = STATE_NORMAL; // 主题状态
    private Integer seq; // 主题序号
}
