package com.project.pet.user.dto.user;

import lombok.Data;

@Data
public class UserLogoutRequest {
    private String refreshToken;
}
