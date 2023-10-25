package com.swx.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@TableName("strategy_catalog")
public class StrategyCatalog {
    public static final int STATE_NORMAL = 1;
    public static final int STATE_DISABLE = 0;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name; // 类别名称
    private Long dest_id; // 目的地
    private String dest_name; // 目的地名称
    private Integer state = STATE_NORMAL; // 类别状态
    private Integer seq; // 类别序号

    private List<Strategy> strategies = new ArrayList<>();
}
