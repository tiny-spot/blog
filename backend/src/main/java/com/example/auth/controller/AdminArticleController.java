package com.example.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auth.entity.Article;
import com.example.auth.entity.User;
import com.example.auth.mapper.ArticleMapper;
import com.example.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/article")
@RequiredArgsConstructor
public class AdminArticleController {
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body,
                                    Authentication auth) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("email", auth.getName()));

        Article a = new Article();
        a.setTitle(body.get("title"));
        a.setContent(body.get("content"));
        a.setAuthorId(user.getId());
        articleMapper.insert(a);
        return ResponseEntity.ok(Map.of("message", "发布成功", "id", a.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Map<String, String> body) {
        Article a = articleMapper.selectById(id);
        if (a == null) return ResponseEntity.notFound().build();
        a.setTitle(body.get("title"));
        a.setContent(body.get("content"));
        articleMapper.updateById(a);
        return ResponseEntity.ok(Map.of("message", "更新成功"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        articleMapper.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "删除成功"));
    }
}
