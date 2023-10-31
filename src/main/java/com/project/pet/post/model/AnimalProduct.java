package com.project.pet.post.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum AnimalProduct {

    HARNESSES("하네스"),
    LEASHES("목줄)"),
    CLOTHES("옷"),
    TOYS("장난감");

    private final String text;
}
