package com.project.pet.global.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueToken {
    @NotBlank(message = "rct 값이 비어 있습니다.")
    private String refreshToken;
}
