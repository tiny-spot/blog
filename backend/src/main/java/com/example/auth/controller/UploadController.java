package com.example.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/upload")
public class UploadController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "文件为空"));
        }

        String original = Objects.requireNonNull(file.getOriginalFilename());
        String ext = original.contains(".")
                ? original.substring(original.lastIndexOf('.'))
                : "";
        String filename = UUID.randomUUID() + ext;

        Path dir = Paths.get(UPLOAD_DIR);
        Files.createDirectories(dir);
        Files.copy(file.getInputStream(), dir.resolve(filename));

        return ResponseEntity.ok(Map.of("url", "/uploads/" + filename));
    }
}
