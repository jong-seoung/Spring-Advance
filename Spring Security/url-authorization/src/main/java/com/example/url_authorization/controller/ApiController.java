package com.example.url_authorization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/posts")
    public ResponseEntity<List<Map<String, Object>>> getPosts() {
        List<Map<String, Object>> posts = Arrays.asList(
                Map.of("id", 1, "title", "공개 게시글 1", "author", "user1"),
                Map.of("id", 2, "title", "공개 게시글 2", "author", "user2")
        );
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Map<String, Object>> getPost(@PathVariable Long id) {
        Map<String, Object> post = Map.of(
                "id", id,
                "title", "게시글 " + id,
                "content", "공개 게시글 내용",
                "author", "user" + id
        );
        return ResponseEntity.ok(post);
    }

    @PostMapping("/posts")
    public ResponseEntity<Map<String, Object>> createPost(
            @RequestBody Map<String, Object> postData,
            Authentication auth
    ) {
        Map<String, Object> result = new HashMap<>(postData);
        result.put("author", auth.getName());
        result.put("message", "게시글이 생성되었습니다. (USER 권한)");

        return ResponseEntity.ok(result);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(
            @PathVariable Long id,
            @RequestBody Map<String, Object> postData,
            Authentication auth
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("message", "게시글이 수정되었습니다. (USER 권한)");
        result.put("updatedBy", auth.getName());

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(
            @PathVariable Long id,
            Authentication auth
    ) {
        Map<String, Object> result = Map.of(
                "id", id,
                "message", "게시글이 삭제되었습니다. (ADMIN 권한)",
                "deletedBy", auth.getName()
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin/stats")
    public ResponseEntity<Map<String, Object>> getStats(Authentication auth) {
        Map<String, Object> stats = Map.of(
                "totalUsers", 150,
                "totalPosts", 1250,
                "totalComments", 3500,
                "requestedBy", auth.getName(),
                "message", "관리자 통계 (ADMIN 권한)"
        );

        return ResponseEntity.ok(stats);
    }
}