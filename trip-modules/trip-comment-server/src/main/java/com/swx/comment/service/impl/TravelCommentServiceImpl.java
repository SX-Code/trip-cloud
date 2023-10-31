package com.swx.comment.service.impl;

import com.swx.comment.domain.StrategyComment;
import com.swx.comment.domain.TravelComment;
import com.swx.comment.qo.CommentQuery;
import com.swx.comment.repository.StrategyCommentRepository;
import com.swx.comment.service.StrategyCommentService;
import com.swx.comment.service.TravelCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelCommentServiceImpl implements TravelCommentService {

    private final StrategyCommentRepository strategyCommentRepository;
    private final MongoTemplate mongoTemplate;

    public TravelCommentServiceImpl(StrategyCommentRepository strategyCommentRepository, MongoTemplate mongoTemplate) {
        this.strategyCommentRepository = strategyCommentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 分页查询
     *
     * @param qo 分页参数
     * @return 评论
     */
    @Override
    public Page<TravelComment> page(CommentQuery qo) {
        // 拼接查询条件
        Criteria criteria = Criteria.where("travelId").is(qo.getArticleId());
        // 创建查询对象，关联条件
        Query query = new Query();
        query.addCriteria(criteria);
        // 统计总数
        long total = mongoTemplate.count(query, TravelComment.class);
        if (total == 0) {
            return Page.empty();
        }
        // 设置分页参数
        PageRequest request = PageRequest.of(qo.getCurrent() - 1, qo.getSize());
        query.skip(request.getOffset()).limit(request.getPageSize());
        // 按照时间排序
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        // 查询数据
        List<TravelComment> records = mongoTemplate.find(query, TravelComment.class);
        return new PageImpl<>(records, request, total);
    }
}
