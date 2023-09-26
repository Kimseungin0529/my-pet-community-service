package com.project.pet.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("회원", "ROLE_USER"),
    ADMIN("관리자", "ROLE_ADMAIN");

    private final String description;
    private final String value;
}
