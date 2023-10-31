package com.project.pet.post.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum AnimalGroup {
    
    DOG("강아지"),
    CAT("고양이");

    private final String text;
}
