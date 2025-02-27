package com.resume.backend.interceptor;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.resume.backend.constants.RedisConstants;
import com.resume.backend.constants.Status;
import com.resume.backend.entity.User;
import com.resume.backend.exception.BizException;
import com.resume.backend.utils.UserHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * jwt拦截器
 *
 * springmvc 拦截器
 *
 * 处理token检验
 *
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    RedisTemplate<String, User> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (!StringUtils.hasLength(token)) {
            throw new BizException(Status.TOKEN_ERROR, "token失效,请重新登陆");
        }

        User user = redisTemplate.opsForValue().get(RedisConstants.USER_TOKEN_KEY + token);

        if (user == null) {
            throw new BizException(Status.TOKEN_ERROR, "token失效,请重新登陆");
        }

        // 存入 ThreadLocal
        UserHolder.saveUser(user);
        // 存入 request，供 Controller 直接使用
        request.setAttribute("userId", user.getId());

        // 重置过期时间
        redisTemplate.expire(RedisConstants.USER_TOKEN_KEY + token, RedisConstants.USER_TOKEN_TTL, TimeUnit.MINUTES);

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getNickname())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new BizException(Status.TOKEN_ERROR, "token验证失败，请重新登陆");
        }

        return true;
    }

}
