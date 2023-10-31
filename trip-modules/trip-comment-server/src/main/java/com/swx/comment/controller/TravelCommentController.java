package com.swx.comment.controller;

import com.swx.comment.service.StrategyCommentService;
import com.swx.comment.service.TravelCommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategies/travels")
public class TravelCommentController {

    private final TravelCommentService travelCommentService;

    public TravelCommentController(TravelCommentService travelCommentService) {
        this.travelCommentService = travelCommentService;
    }
}
