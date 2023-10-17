package com.project.pet.user.exception;

import com.project.pet.global.common.exception.CustomException;
import com.project.pet.global.common.exception.ErrorType;

import static com.project.pet.global.common.exception.ErrorType.NotFound.USER_NOT_FOUND;

public class UserNotFoundException extends CustomException.NotFoundException {

    public UserNotFoundException(ErrorType.NotFound errorType, String detail) {
        super(errorType, detail);
    }
    public UserNotFoundException() {
        super(USER_NOT_FOUND, "해당 아이디를 가진 회원을 찾을 수 없습니다.");
    }

}
