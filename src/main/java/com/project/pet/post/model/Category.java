package com.project.pet.post.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum Category {
    COMUNITY( "커뮤니티"),
    DEAL("거래");

    private final String text;
}
