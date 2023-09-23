package com.project.pet.reply.model;

import com.project.pet.post.model.Post;
import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
public class Reply {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long replyId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = LAZY )
    private Post post;

    @Column(nullable = false)
    private String content;
}
