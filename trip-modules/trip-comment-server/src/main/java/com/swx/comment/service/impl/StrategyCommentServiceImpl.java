package com.swx.comment.service.impl;

import com.swx.comment.domain.StrategyComment;
import com.swx.comment.key.CommentRedisKeyPrefix;
import com.swx.comment.qo.CommentQuery;
import com.swx.comment.repository.StrategyCommentRepository;
import com.swx.comment.service.StrategyCommentService;
import com.swx.common.core.exception.BizException;
import com.swx.common.redis.service.RedisService;
import com.swx.common.security.util.AuthenticationUtil;
import com.swx.common.security.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {

    private final StrategyCommentRepository strategyCommentRepository;
    private final MongoTemplate mongoTemplate;
    private final RedisService redisService;

    public StrategyCommentServiceImpl(StrategyCommentRepository strategyCommentRepository, MongoTemplate mongoTemplate, RedisService redisService) {
        this.strategyCommentRepository = strategyCommentRepository;
        this.mongoTemplate = mongoTemplate;
        this.redisService = redisService;
    }

    /**
     * 分页查询
     *
     * @param qo 分页参数
     * @return 评论
     */
    @Override
    public Page<StrategyComment> page(CommentQuery qo) {
        // 拼接查询条件
        Criteria criteria = Criteria.where("strategyId").is(qo.getArticleId());
        // 创建查询对象，关联条件
        Query query = new Query();
        query.addCriteria(criteria);
        // 统计总数
        long total = mongoTemplate.count(query, StrategyComment.class);
        if (total == 0) {
            return Page.empty();
        }
        // 设置分页参数
        PageRequest request = PageRequest.of(qo.getCurrent() - 1, qo.getSize());
        query.skip(request.getOffset()).limit(request.getPageSize());
        // 按照时间排序
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        // 查询数据
        List<StrategyComment> records = mongoTemplate.find(query, StrategyComment.class);
        return new PageImpl<>(records, request, total);
    }

    /**
     * 保存评论
     *
     * @param comment    攻略评论
     */
    @Override
    public void save(StrategyComment comment) {
        // 获取当前登陆用户
        LoginUser loginUser = AuthenticationUtil.getLoginUser();
        if (loginUser == null) {
            log.error("[攻略评论模块] 获取登陆用户信息错误");
            throw new BizException("获取登陆用户信息错误");
        }
        comment.setUserId(loginUser.getId());
        comment.setNickname(loginUser.getNickname());
        comment.setCity(loginUser.getCity());
        comment.setLevel(loginUser.getLevel());
        comment.setHeadImgUrl(loginUser.getHeadImgUrl());
        comment.setCreateTime(new Date());
        // 保存到 mongodb
        strategyCommentRepository.save(comment);
    }

    /**
     * 点赞和取消点赞
     *
     * @param cid 评论ID
     */
    @Override
    public void doLike(String cid) {
        // 基于 cid 查询评论对象
        Optional<StrategyComment> optional = strategyCommentRepository.findById(cid);
        if (optional.isPresent()) {
            StrategyComment strategyComment = optional.get();
            // 获取当前登陆用户对象
            LoginUser loginUser = AuthenticationUtil.getLoginUser();
            // 判断当前用户是否点赞
            if (strategyComment.getThumbuplist().contains(loginUser.getId())) {
                // 如果点赞：点赞数-1，将用户 id 从集合中删除
                strategyComment.setThumbupnum(strategyComment.getThumbupnum() - 1);
                strategyComment.getThumbuplist().remove(loginUser.getId());
            } else {
                // 如果没点赞：点赞数+1， 将用户 id 添加到集合中
                strategyComment.setThumbupnum(strategyComment.getThumbupnum() + 1);
                strategyComment.getThumbuplist().add(loginUser.getId());
            }
            // 重新将对象保存到 mongodb
            strategyCommentRepository.save(strategyComment);
        }
    }

    /**
     * 评论数+1
     *
     * @param strategyId 攻略ID
     */
    @Override
    public void replyNumIncr(Long strategyId) {
        redisService.hashIncrement(CommentRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP, "replynum", 1, strategyId + "");
    }
}
