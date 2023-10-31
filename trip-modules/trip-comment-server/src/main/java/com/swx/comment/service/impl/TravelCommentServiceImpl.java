package com.swx.comment.service.impl;

import com.swx.comment.domain.StrategyComment;
import com.swx.comment.domain.TravelComment;
import com.swx.comment.qo.CommentQuery;
import com.swx.comment.repository.StrategyCommentRepository;
import com.swx.comment.repository.TravelCommentRepository;
import com.swx.comment.service.StrategyCommentService;
import com.swx.comment.service.TravelCommentService;
import com.swx.common.core.exception.BizException;
import com.swx.common.security.util.AuthenticationUtil;
import com.swx.common.security.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TravelCommentServiceImpl implements TravelCommentService {

    private final TravelCommentRepository travelCommentRepository;
    private final MongoTemplate mongoTemplate;

    public TravelCommentServiceImpl(TravelCommentRepository travelCommentRepository, MongoTemplate mongoTemplate) {
        this.travelCommentRepository = travelCommentRepository;
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

    /**
     * 保存游记评论
     *
     * @param comment 评论信息
     */
    @Override
    public void save(TravelComment comment) {
        // 获取当前登陆用户
        LoginUser loginUser = AuthenticationUtil.getLoginUser();
        if (loginUser == null) {
            log.error("[游记评论模块] 获取登陆用户信息错误");
            throw new BizException("获取登陆用户信息错误");
        }
        comment.setUserId(loginUser.getId());
        comment.setNickname(loginUser.getNickname());
        comment.setCity(loginUser.getCity());
        comment.setLevel(loginUser.getLevel());
        comment.setHeadImgUrl(loginUser.getHeadImgUrl());
        comment.setCreateTime(new Date());
        if (comment.getRefComment() != null && StringUtils.hasLength(comment.getRefComment().getId())) {
            // 评论的评论
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE);
        } else {
            // 普通评论
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE_COMMENT);
        }
        travelCommentRepository.save(comment);
    }

    /**
     * 获取游记的评论
     *
     * @param travelId 游记ID
     * @return 所有评论
     */
    @Override
    public List<TravelComment> findList(Long travelId) {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "createTime"))
                .addCriteria(Criteria.where("travelId").is(travelId));
        List<TravelComment> travelComments = mongoTemplate.find(query, TravelComment.class);
        for (TravelComment travelComment : travelComments) {
            TravelComment refComment = travelComment.getRefComment();
            if (refComment != null && refComment.getId() != null) {
                Optional<TravelComment> refCommentOptional = travelCommentRepository.findById(refComment.getId());
                travelComment.setRefComment(refCommentOptional.orElse(null));
            }
        }
        return travelComments;
    }
}
