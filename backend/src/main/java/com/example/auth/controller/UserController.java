package com.example.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 获取当前登录用户信息
     * 需要携带 Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        // authentication.getName() 就是 JWT 过滤器里放进 SecurityContext 的 email
        String email = authentication.getName();
        return ResponseEntity.ok(Map.of(
                "email", email,
                "message", "这是你的个人信息",
                "time", LocalDateTime.now().toString()
        ));
    }

    /**
     * 模拟一个业务操作，同样需要鉴权
     */
    @PostMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> body,
                                           Authentication authentication) {
        String email = authentication.getName();
        String nickname = body.get("nickname");
        return ResponseEntity.ok(Map.of(
                "email", email,
                "nickname", nickname,
                "message", "资料更新成功（示例，未真正入库）"
        ));
    }
}