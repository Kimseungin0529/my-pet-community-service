package com.project.pet.favorite.model;

import com.project.pet.post.model.Post;
import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter @Entity
public class Favorite {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long favoriteId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = LAZY)
    private Post post;
}
