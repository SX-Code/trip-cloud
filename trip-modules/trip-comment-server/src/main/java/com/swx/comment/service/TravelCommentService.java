package com.swx.comment.service;

import com.swx.comment.domain.TravelComment;
import com.swx.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

public interface TravelCommentService {

    /**
     * 分页查询
     * @param qo 分页参数
     * @return 评论
     */
    Page<TravelComment> page(CommentQuery qo);
}
