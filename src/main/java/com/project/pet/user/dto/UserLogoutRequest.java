package com.project.pet.user.dto;

import lombok.Data;

@Data
public class UserLogoutRequest {
    private String refreshToken;
}
