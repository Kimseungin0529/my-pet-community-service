package com.project.pet.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("회원", "user_role"),
    ADMIN("관리자", "admin_role");

    private final String description;
    private final String value;
}
