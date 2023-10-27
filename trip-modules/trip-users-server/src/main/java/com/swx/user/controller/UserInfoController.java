package com.swx.user.controller;

import com.swx.common.core.utils.R;
import com.swx.user.dto.UserInfoDTO;
import com.swx.user.service.UserInfoService;
import com.swx.user.vo.RegisterRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    @PostMapping("/register")
    public R<?> register(RegisterRequest req) {
        userInfoService.register(req);
        return R.ok();
    }

    @PostMapping("/login")
    public R<Map<String, Object>> login(String username, String password) {
        Map<String, Object> map = userInfoService.login(username, password);
        return R.ok(map);
    }

    @GetMapping("/getById")
    public R<UserInfoDTO> getById(Long id) {
        return R.ok(userInfoService.getDtoById(id));
    }
}
