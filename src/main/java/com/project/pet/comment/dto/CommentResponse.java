package com.project.pet.comment.dto;

import com.project.pet.post.model.Post;
import com.project.pet.user.model.User;
import lombok.Getter;

@Getter
public class CommentResponse {
    private Long commentId;

    private User user;

    private Post post;

    private String content;
}
