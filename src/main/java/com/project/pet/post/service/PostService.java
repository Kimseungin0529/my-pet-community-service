package com.project.pet.post.service;

import com.project.pet.comment.dto.CommentResponse;
import com.project.pet.post.dto.PostCreateRequest;
import com.project.pet.post.dto.PostDetailResponse;
import com.project.pet.post.dto.PostTotalResponse;
import com.project.pet.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public void createPost(PostCreateRequest postCreateRequest){

        return;
    }

    public PostDetailResponse modifyPost(){
        return null;
    }
    public PostDetailResponse getPost(){
        return null;
    }
    public PostTotalResponse getPosts(){
        return null;
    }
    public void removePost(){

    }
}
