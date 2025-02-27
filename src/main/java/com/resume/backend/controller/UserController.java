package com.resume.backend.controller;

import com.resume.backend.DTO.LoginForm;
import com.resume.backend.common.R;
import com.resume.backend.entity.User;
import com.resume.backend.service.UserService;
import com.resume.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public R weChatLogin(@RequestBody LoginForm loginForm) {
        return R.success(userService.loginOrRegisterByWeChatCode(loginForm));
    }
}
