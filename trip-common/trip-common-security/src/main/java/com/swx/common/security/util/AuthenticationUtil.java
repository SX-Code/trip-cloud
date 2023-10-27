package com.swx.common.security.util;

import com.swx.common.core.exception.BizException;
import com.swx.common.security.service.TokenService;
import com.swx.common.security.vo.LoginUser;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationUtil {

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

    public static LoginUser getLoginUser() {
        String token = getToken();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        TokenService tokenService = SpringContextUtil.getBean(TokenService.class);
        return tokenService.getLoginUser(token);
    }
}
