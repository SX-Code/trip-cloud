package com.swx.comment.service;

import com.swx.comment.domain.StrategyComment;
import com.swx.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

public interface StrategyCommentService {

    /**
     * 分页查询
     *
     * @param qo 分页参数
     * @return 评论
     */
    Page<StrategyComment> page(CommentQuery qo);

    /**
     * 保存评论
     *
     * @param comment    攻略评论
     */
    void save(StrategyComment comment);

    /**
     * 点赞和取消点赞
     *
     * @param cid 评论ID
     */
    void doLike(String cid);

    /**
     * 评论数+1
     *
     * @param strategyId 攻略ID
     */
    void replyNumIncr(Long strategyId);
}
