package com.swx.common.security.configure;

import com.swx.common.security.interceptor.LoginInterceptor;
import com.swx.common.security.service.TokenService;
import com.swx.common.security.util.SpringContextUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebConfig.class)
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAutoConfiguration {

    @Bean
    public LoginInterceptor loginInterceptor(TokenService tokenService) {
        return new LoginInterceptor(tokenService);
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
}
