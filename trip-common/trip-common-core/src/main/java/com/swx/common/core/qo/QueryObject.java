package com.swx.common.core.qo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QueryObject {
    private String keyword;
    private Integer current = 1;
    private Integer size = 10;

    public QueryObject(Integer current, Integer size) {
        this.current = current;
        this.size = size;
    }

    public Integer getOffset() {
        return (current - 1) * size;
    }

}
