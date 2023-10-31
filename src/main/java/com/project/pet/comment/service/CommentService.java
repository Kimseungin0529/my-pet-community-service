package com.project.pet.comment.service;

import com.project.pet.comment.dto.CommentResponse;
import com.project.pet.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment(){
    }

    public CommentResponse modifyComment(){
        return null;
    }
    public CommentResponse getComment(){
        return null;
    }
    public void removeComment(){

    }

}
