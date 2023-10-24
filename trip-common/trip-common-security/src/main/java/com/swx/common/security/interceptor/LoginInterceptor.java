package com.swx.common.security.interceptor;

import com.swx.common.core.exception.BizException;
import com.swx.common.security.annotation.RequireLogin;
import com.swx.common.security.service.TokenService;
import com.swx.common.security.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public LoginInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            // handler => 静态资源
            // handler => CORS 的预请求
            return true;
        }
        // 将 handler 对象转换为 HandlerMethod 对象
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从 HandlerMethod 对象中获取对应的 Controller 对象
        Class<?> clazz = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        // 从 Controller 和 HandlerMethod 上获取 @RequireLogin 注解
        if (clazz.isAnnotationPresent(RequireLogin.class) || method.isAnnotationPresent(RequireLogin.class)) {
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
        }
        return true;
    }
}
