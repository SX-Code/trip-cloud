package com.swx.comment.service;

import com.swx.comment.domain.TravelComment;
import com.swx.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TravelCommentService {

    /**
     * 分页查询
     * @param qo 分页参数
     * @return 评论
     */
    Page<TravelComment> page(CommentQuery qo);

    /**
     * 保存游记评论
     * @param comment 评论信息
     */
    void save(TravelComment comment);

    /**
     * 获取游记的评论
     * @param travelId 游记ID
     * @return 所有评论
     */
    List<TravelComment> findList(Long travelId);
}
