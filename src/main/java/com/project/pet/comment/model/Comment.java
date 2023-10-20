package com.project.pet.comment.model;

import com.project.pet.post.model.Post;
import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
public class Comment {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long commentId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = LAZY)
    private Post post;

    @Column(nullable = false)
    private String content;

    public void registerPost(Post post) {
        this.post = post;
    }




}
