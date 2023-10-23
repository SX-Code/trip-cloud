package com.swx.common.security.service;

import com.swx.common.redis.service.RedisService;
import com.swx.common.security.configure.JwtProperties;
import com.swx.common.security.key.UserRedisKeyPrefix;
import com.swx.common.security.vo.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 */
@Component
public class TokenService {
    public static final String TOKEN_HEADER = "Token";
    private static final String LOGIN_USER_REDIS_UUID = "uuid";
    private static final long MINUTES_MILLISECONDS = 60 * 1000L;
    private static final long TWENTY_MILLISECONDS = 20 * MINUTES_MILLISECONDS;
    private final SecretKey key;

    private final JwtProperties jwtProperties;
    private final RedisService redisService;

    public TokenService(JwtProperties jwtProperties, RedisService redisService) {
        this.jwtProperties = jwtProperties;
        this.redisService = redisService;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    /**
     * 创建 Token
     * @param loginUser 用户信息
     * @return token
     */
    public String createToken(LoginUser loginUser) {
        // 设置登陆时间和过期时间
        long now = System.currentTimeMillis();
        loginUser.setLoginTime(now);
        long expireTime = now + jwtProperties.getExpireTime() * MINUTES_MILLISECONDS;
        loginUser.setExpireTime(expireTime);

        // 生成UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        loginUser.setToken(uuid);

        // 将用户信息缓存到Redis中, 设置过期时间
        String redisKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid);
        redisService.setCacheObject(redisKey, loginUser, expireTime, TimeUnit.MICROSECONDS);

        // 4. 使用JWT生成TOKEN，存入基础信息
        return Jwts.builder()
                .id(loginUser.getId().toString())
                .claim(LOGIN_USER_REDIS_UUID, uuid)
                .signWith(key).compact();
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        // 判断 Token 是否有效
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
        Claims body = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        String uuid = (String) body.get(LOGIN_USER_REDIS_UUID);

        // 从Redis中获取用户对象
        String redisKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid);
        return redisService.getCacheObject(redisKey);
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        long loginTime;
        if ((loginUser.getExpireTime() - (loginTime = System.currentTimeMillis())) <= TWENTY_MILLISECONDS) {
            // 如果用户过期剩余时间小于20分钟，刷新过期时间
            loginUser.setLoginTime(loginTime);
            long expireTime = loginTime + jwtProperties.getExpireTime() * MINUTES_MILLISECONDS;
            loginUser.setExpireTime(expireTime);
            // 将刷新后的时间覆盖Redis
            String redisKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(loginUser.getToken());
            redisService.setCacheObject(redisKey, loginUser, expireTime, TimeUnit.MICROSECONDS);
        }
    }

    public static void main(String[] args) {
        Key key = Jwts.SIG.HS256.key().build();
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secretString);
    }
}
