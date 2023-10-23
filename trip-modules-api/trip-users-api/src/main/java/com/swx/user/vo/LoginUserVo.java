package com.swx.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserVo {

    private Long id;
    private String nickname; // 昵称
    private String phone; // 手机
    private String email; // 邮箱
    private Integer gender; // 性别
    private String city; // 所在城市
    private String headImgUrl; // 头像
    private String info; // 个性签名
    private Long loginTime; // 登陆时间
    private Long expireTime; // 过期时间
    private String token;
}
