package com.resume.backend.entity;

import lombok.Data;

@Data
public class UserRe {
    /**
     * 用户id
     */
    private int id;

    /**
     * 用户微信开放id
     */
    private String openid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * token
     */
    private String token;
}
