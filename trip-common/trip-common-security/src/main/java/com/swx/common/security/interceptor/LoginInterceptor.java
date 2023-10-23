package com.swx.common.security.interceptor;

import com.swx.common.core.exception.BizException;
import com.swx.common.security.service.TokenService;
import com.swx.common.security.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public LoginInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取 jwt token
        String token = request.getHeader(TokenService.TOKEN_HEADER);
        // 2. 基于 jwt sdk 解析 token，解析失败
        try {
            LoginUser loginUser = tokenService.getLoginUser(token);
            if (loginUser == null) {
                throw new BizException(401, "Token 已失效");
            }
            // 未失效，刷新Token
            tokenService.refreshToken(loginUser);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.warn("[登陆拦截] jwt token 解析失败");
            throw new BizException(401, "用户未认证");
        }
        return true;
    }
}
