package com.swx.comment.controller;

import com.swx.comment.domain.StrategyComment;
import com.swx.comment.qo.CommentQuery;
import com.swx.comment.service.StrategyCommentService;
import com.swx.common.core.exception.BizException;
import com.swx.common.core.utils.R;
import com.swx.common.security.annotation.RequireLogin;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategies/comments")
public class StrategyCommentController {

    private final StrategyCommentService strategyCommentService;

    public StrategyCommentController(StrategyCommentService strategyCommentService) {
        this.strategyCommentService = strategyCommentService;
    }

    @GetMapping("/query")
    public R<Page<StrategyComment>> saveComment(CommentQuery query) {
        return R.ok(strategyCommentService.page(query));
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> saveComment(Long strategyId, String strategyTitle) {
        strategyCommentService.save(strategyId, strategyTitle);
        return R.ok();
    }
}
