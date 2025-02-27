package com.resume.backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.backend.DTO.LoginForm;
import com.resume.backend.constants.RedisConstants;
import com.resume.backend.entity.User;
import com.resume.backend.entity.UserRe;
import com.resume.backend.mapper.UserMapper;
import com.resume.backend.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    @Resource
    private RedisTemplate<String, User> redisTemplate;

    private final UserMapper userMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 微信小程序的 AppID 和 AppSecret（建议放在配置文件中）
    private final String WECHAT_APP_ID = "wxaed9ec68e53c3875";
    private final String WECHAT_APP_SECRET = "7cfe2ed5ad2cc3b53a3a8448ea63fbd0";

    /**
     * 微信登录或注册，使用 LoginForm 作为参数。
     * 调用微信接口获取 openid，然后查找或注册用户，再生成 token 并存入 Redis，
     * 最后返回包含 token 的用户信息。
     */
    public Object loginOrRegisterByWeChatCode(LoginForm loginForm) {
        // 获取微信登录的 code、昵称、头像
        String code = loginForm.getCode();
        String nickname = loginForm.getUserInfo().getNickName();
        String avatarUrl = loginForm.getUserInfo().getAvatarUrl();

        // 构造微信 API URL
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + WECHAT_APP_ID
                + "&secret=" + WECHAT_APP_SECRET
                + "&js_code=" + code
                + "&grant_type=authorization_code";
        System.out.println("调用微信接口 URL: " + url);

        // 调用微信接口
        String response = restTemplate.getForObject(url, String.class);
        String openid;
        try {
            JsonNode root = objectMapper.readTree(response);
            if (root.has("openid")) {
                openid = root.get("openid").asText();
            } else {
                throw new RuntimeException("微信登录失败，返回数据：" + response);
            }
        } catch (Exception e) {
            throw new RuntimeException("解析微信返回数据失败", e);
        }

        // 查找用户（根据 openid），若不存在则注册新用户
        User user = userMapper.findByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname);
            user.setAvatarUrl(avatarUrl);
            userMapper.insertUser(user);
        } else {
            // 更新用户信息（可选，根据实际需求更新）
            if (nickname != null) {
                user.setNickname(nickname);
            }
            if (avatarUrl != null) {
                user.setAvatarUrl(avatarUrl);
            }
            // 若有 updateUser 方法，可调用 userMapper.updateUser(user);
        }

        // 生成 token（例如使用 openid 和昵称生成）
        String token = TokenUtils.genToken(user.getOpenid(), user.getNickname());
        // 将用户信息存入 Redis，key 格式由 RedisConstants.USER_TOKEN_KEY + token 构成
        String redisKey = RedisConstants.USER_TOKEN_KEY + token;
        redisTemplate.opsForValue().set(redisKey, user);
        // 仅设置 Redis 过期时间，不依赖 JWT 自身的过期设置
        redisTemplate.expire(redisKey, RedisConstants.USER_TOKEN_TTL, TimeUnit.MINUTES);

        // 将 user 的部分属性拷贝到 UserRe 对象中，并设置 token
        UserRe userRe = BeanUtil.copyProperties(user, UserRe.class);
        userRe.setToken(token);

        return userRe;
    }
}
