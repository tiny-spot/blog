package com.example.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auth.entity.User;
import com.example.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitRunner implements CommandLineRunner {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Long count = userMapper.selectCount(
                new QueryWrapper<User>().eq("role", "ADMIN"));
        if (count == 0) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userMapper.insert(admin);
            System.out.println("✅ 默认管理员已创建：admin@example.com / admin123");
        }
    }
}
