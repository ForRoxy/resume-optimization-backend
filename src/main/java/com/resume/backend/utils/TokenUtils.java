package com.resume.backend.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.resume.backend.constants.Status;
import com.resume.backend.entity.User;
import com.resume.backend.exception.BizException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * token 工具
 *
 * @author: ShanZhu
 * @date: 2023-11-10
 */
@Component
public class TokenUtils {

    /**
     * 生成token  java token
     *
     * @param userId 用户id
     * @param username 用户名
     * @return token
     */
    public static String genToken(String userId, String username) {
        String token = JWT.create()
                .withAudience(userId)
                .sign(Algorithm.HMAC256(username));
        return token;
    }

    /**
     * 获取当前用户
     *
     * @return 用户
     */
    public static User getCurrentUser() {
        try {
            return UserHolder.getUser();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验是否登录
     *
     * @return 结果
     */
    public static boolean validateLogin() {
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader("token");
            if (StringUtils.hasLength(token)) {
                JWT.decode(token).getAudience();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new BizException(Status.CODE_401, "登录状态失效！");
        }
    }


}

