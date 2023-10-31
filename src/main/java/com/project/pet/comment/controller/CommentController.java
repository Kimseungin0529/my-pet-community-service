package com.project.pet.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/comment")
public class CommentController {
    @PostMapping("/")
    public ResponseEntity<?> createComment(){
        return null;
    }
    // 수정
    @PatchMapping("/{comment-id}")
    public ResponseEntity<?> modifyComment(){
        return null;
    }
    // 단일 조회
    @GetMapping("/")
    public ResponseEntity<?> getComment(){
        return null;
    }
    // 삭제
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<?> removeComment(){
        return null;
    }
}
