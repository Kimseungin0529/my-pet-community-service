package com.project.pet.post.model;

import com.project.pet.comment.model.Comment;
import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
public class Post {
    @Id @GeneratedValue(strategy =  IDENTITY)
    private Long postId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // 삭제 외는 영속성 전이 필요성을 느끼지 못함. 추후 느끼면 리팩토링
    private List<Comment> comments = new ArrayList<>();

    private int heartCount  ;

    private int readCount;

    @Enumerated(value = STRING)
    private Category category;

    @Enumerated(value = STRING)
    private AnimalGroup animalGroup;

    @Enumerated(value = STRING)
    private AnimalProduct animalProduct;



    public void addComments(Comment comment){ // 연관관계 편의 메소드
        this.comments.add(comment);
        comment.registerPost(this);
    }
}
