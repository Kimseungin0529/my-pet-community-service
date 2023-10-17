package com.project.pet.global.auth;

import com.project.pet.global.common.exception.CustomException;
import com.project.pet.global.common.exception.ErrorType;

public class LoginFailException extends CustomException.UnauthorizedException {
    public LoginFailException() {
        super(ErrorType.Unauthorized.LOGIN_UNAUTHORIZE, "아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
