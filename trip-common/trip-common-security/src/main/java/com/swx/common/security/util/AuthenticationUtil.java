package com.swx.common.security.util;

import com.swx.common.core.exception.BizException;
import com.swx.common.security.service.TokenService;
import com.swx.common.security.vo.LoginUser;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtil {
    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new BizException("该方法只能在Spring MVC 环境下调用");
        }
        return requestAttributes.getRequest();
    }

    public static String getToken() {
        return getRequest().getHeader(TokenService.TOKEN_HEADER);
    }

    /**
     * 从Token或者缓存中获取User
     * @return 登陆用户
     */
    public static LoginUser getLoginUser() {
        LoginUser cacheUser = USER_HOLDER.get();
        if (cacheUser != null) {
            return cacheUser;
        }
        String token = getToken();
        if (StringUtils.isEmpty(token) || token.equals("undefined")) {
            return null;
        }
        TokenService tokenService = SpringContextUtil.getBean(TokenService.class);
        LoginUser loginUser = tokenService.getLoginUser(token);
        USER_HOLDER.set(loginUser);
        return loginUser;
    }

    /**
     * 清空缓存中的User
     */
    public static void removeUser() {
        USER_HOLDER.remove();
    }
}
