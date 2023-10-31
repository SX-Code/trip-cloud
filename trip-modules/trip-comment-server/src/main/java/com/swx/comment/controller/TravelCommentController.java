package com.swx.comment.controller;

import com.swx.comment.domain.TravelComment;
import com.swx.comment.service.TravelCommentService;
import com.swx.common.core.utils.R;
import com.swx.common.security.annotation.RequireLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travels/comments")
public class TravelCommentController {

    private final TravelCommentService travelCommentService;

    public TravelCommentController(TravelCommentService travelCommentService) {
        this.travelCommentService = travelCommentService;
    }

    @GetMapping("/query")
    public R<List<TravelComment>> findList(Long travelId) {
        return R.ok(travelCommentService.findList(travelId));
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> saveComment(TravelComment comment) {
        travelCommentService.save(comment);
        return R.ok();
    }
}
