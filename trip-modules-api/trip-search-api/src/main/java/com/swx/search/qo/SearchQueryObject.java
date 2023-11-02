package com.swx.search.qo;

import com.swx.common.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchQueryObject extends QueryObject {
    private Integer type = -1;
}
