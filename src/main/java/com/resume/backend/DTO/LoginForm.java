package com.resume.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    private String code;  // 微信登录 code
    private UserInfo userInfo; // 用户信息

}

