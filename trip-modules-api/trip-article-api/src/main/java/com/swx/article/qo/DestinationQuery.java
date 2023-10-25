package com.swx.article.qo;

import com.swx.common.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationQuery extends QueryObject {
    private Long parentId;
}
