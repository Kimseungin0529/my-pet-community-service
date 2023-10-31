package com.project.pet.post.dto;

import com.project.pet.comment.model.Comment;
import com.project.pet.post.model.AnimalGroup;
import com.project.pet.post.model.AnimalProduct;
import com.project.pet.post.model.Category;
import com.project.pet.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
public class PostCreateRequest {
    private Long postId;
    @NotBlank(message = "제목이 비어 있습니다.")
    private String title;
    @NotBlank(message = "본문이 비어 있습니다.")
    private String content;
    @NotBlank(message = "커뮤티니/거래 중 선택해주세요.")
    private String category;

    private String animalGroup;

    private String animalProduct;



    @Builder
    public PostCreateRequest(String title, String content, String category, String animalGroup, String animalProduct) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.animalGroup = animalGroup;
        this.animalProduct = animalProduct;
    }
}
