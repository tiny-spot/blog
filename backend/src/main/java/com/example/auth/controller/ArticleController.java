package com.example.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auth.entity.Article;
import com.example.auth.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleMapper articleMapper;

    @GetMapping("/list")
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword) {

        Page<Article> pageParam = new Page<>(page, size);

        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");

        Page<Article> result = articleMapper.selectPage(pageParam, wrapper);

        Map<String, Object> resp = new HashMap<>();
        resp.put("records", result.getRecords());
        resp.put("total", result.getTotal());
        resp.put("page", result.getCurrent());
        resp.put("size", result.getSize());
        resp.put("pages", result.getPages());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(article);
    }
}
