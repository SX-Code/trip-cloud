package com.swx.user.controller;

import com.swx.common.core.utils.R;
import com.swx.user.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/phone/exists")
    public R<Boolean> checkExists(String phone){
        return R.ok(userInfoService.findByPhone(phone) != null);
    }
}
