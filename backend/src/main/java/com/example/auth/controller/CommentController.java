package com.example.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auth.entity.Comment;
import com.example.auth.entity.User;
import com.example.auth.mapper.CommentMapper;
import com.example.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @GetMapping("/{articleId}")
    public ResponseEntity<?> list(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "20") long size) {

        Page<Comment> pageParam = new Page<>(page, size);

        Page<Comment> result = commentMapper.selectPage(pageParam,
                new QueryWrapper<Comment>()
                        .eq("article_id", articleId)
                        .orderByDesc("create_time"));

        List<Map<String, Object>> records = result.getRecords().stream().map(c -> {
            User u = userMapper.selectById(c.getUserId());
            Map<String, Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("content", c.getContent());
            m.put("createTime", c.getCreateTime());
            m.put("email", u != null ? u.getEmail() : "未知");
            return m;
        }).toList();

        Map<String, Object> resp = new HashMap<>();
        resp.put("records", records);
        resp.put("total", result.getTotal());
        resp.put("page", result.getCurrent());
        resp.put("size", result.getSize());
        resp.put("pages", result.getPages());
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Map<String, Object> body,
                                 Authentication auth) {
        Long articleId = Long.valueOf(body.get("articleId").toString());
        String content = body.get("content").toString();

        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("email", auth.getName()));

        Comment c = new Comment();
        c.setArticleId(articleId);
        c.setUserId(user.getId());
        c.setContent(content);
        commentMapper.insert(c);

        return ResponseEntity.ok(Map.of("message", "评论成功"));
    }
}
