package com.swx.comment.qo;

import com.swx.common.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentQuery extends QueryObject {
    private Long articleId;
}
