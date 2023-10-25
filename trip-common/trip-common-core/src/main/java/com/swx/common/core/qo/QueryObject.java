package com.swx.common.core.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryObject {
    private String keyword;
    private Integer current = 1;
    private Integer size = 10;
}
