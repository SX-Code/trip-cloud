package com.swx.comment.controller;

import com.swx.comment.domain.TravelComment;
import com.swx.comment.service.StrategyCommentService;
import com.swx.comment.service.TravelCommentService;
import com.swx.common.core.utils.R;
import com.swx.common.security.annotation.RequireLogin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategies/travels")
public class TravelCommentController {

    private final TravelCommentService travelCommentService;

    public TravelCommentController(TravelCommentService travelCommentService) {
        this.travelCommentService = travelCommentService;
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> saveComment(TravelComment comment) {
        travelCommentService.save(comment);
        return R.ok();
    }
}
