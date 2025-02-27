package com.resume.backend.constants;

/**
 * Redis常量
 *
 * @author: Amadeus
 * @date: 2025-01-01
 */
public class RedisConstants {

    /**
     * 用户token键值
     */
    public static final String USER_TOKEN_KEY = "user:token:";
    public static final Integer USER_TOKEN_TTL = 180;

    /**
     * 商品id键值
     */
    public static final String GOOD_ID_KEY = "good:id:";
    public static final Integer GOOD_ID_TTL = 30;
}
