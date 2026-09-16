package com.example.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auth.entity.User;
import com.example.auth.mapper.UserMapper;
import com.example.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "邮箱已注册"));
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userMapper.insert(user);

        return ResponseEntity.ok(Map.of("message", "注册成功"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "邮箱或密码错误"));
        }

        String token = jwtUtil.generateToken(email, user.getRole());
        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", email,
                "role", user.getRole()
        ));
    }
}
