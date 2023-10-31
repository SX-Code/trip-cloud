package com.swx.comment.repository;

import com.swx.comment.domain.StrategyComment;
import com.swx.comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 游记评论 Repository
 */
public interface TravelCommentRepository extends MongoRepository<TravelComment, String> {
}
