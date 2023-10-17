package com.project.pet.user.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(max = 30,message = "길이는 30 이하로 작성해주세요.")
    private String loginId;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+|<>?:{}])[A-Za-z\\d~!@#$%^&*()_+|<>?:{}]{10,20}$",
            message = "알파벳, 숫자, 특수문자가 각각 1개 이상 들어가게 10 ~ 20 이내로 작성해주세요.")
    private String password;


    private String checkedPassword;

    @Email(message = "이메일 형식으로 작성해주세요.")
    private String email;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "유효한 휴대폰 전화번호 형식이 아닙니다.")
    private String phone;
}
