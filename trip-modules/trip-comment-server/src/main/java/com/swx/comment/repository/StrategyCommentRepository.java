package com.swx.comment.repository;

import com.swx.comment.domain.StrategyComment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 攻略评论 Repository
 */
public interface StrategyCommentRepository extends MongoRepository<StrategyComment, String> {
}
