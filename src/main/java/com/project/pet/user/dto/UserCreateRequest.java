package com.project.pet.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserCreateRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Length(max = 30)
    private String loginId;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+|<>?:{}])[A-Za-z\\d~!@#$%^&*()_+|<>?:{}]{10,20}$",
            message = "알파벳, 숫자, 특수문자가 각각 1개 이상 들어가게 10 ~ 20 이내로 작성해주세요.")
    private String password;

    @Email
    private String email;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "유효한 휴대폰 전화번호 형식이 아닙니다.")
    private String phone;
}
