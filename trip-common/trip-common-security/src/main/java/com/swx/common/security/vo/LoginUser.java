package com.swx.common.security.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {
    private Long id;
    private String nickname; // 昵称
    private Long loginTime; // 登陆时间
    private Long expireTime; // 过期时间
    private String token;
}
