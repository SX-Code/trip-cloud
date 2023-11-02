package com.swx.article.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 目的地（行政地区：国家/省份/城市）
 */
@Getter
@Setter
public class DestinationDTO {
    private Long id;
    private String name;
    private String english;
    private Long parentId;
    private String parentName;
    private String info;
    private String coverUrl;
}
