package com.project.pet.pet.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum Gender {
    MALE("수컷"),
    FEMALE("암컷");

    private final String value;
}
