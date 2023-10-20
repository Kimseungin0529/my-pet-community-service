package com.project.pet.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
public class PostController {

    @PostMapping("/")
    public ResponseEntity<?> createPost(){
        return null;
    }
    // 수정
    @PatchMapping("/{post-id}")
    public ResponseEntity<?> modifyPost(){
        return null;
    }
    // 단일 조회
    @GetMapping("/{post-id}")
    public ResponseEntity<?> getPost(){
        return null;
    }
    // 페이징 조회
    @GetMapping("/")
    public ResponseEntity<?> getPosts(){
        return null;
    }
    // 삭제
    @DeleteMapping("/{{post-id}-id}")
    public ResponseEntity<?> removePost(){
        return null;
    }
}
