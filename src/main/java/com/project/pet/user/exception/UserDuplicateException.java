package com.project.pet.user.exception;

import com.project.pet.global.common.exception.CustomException;
import com.project.pet.global.common.exception.ErrorType;

public class UserDuplicateException extends CustomException.ConflictException {
    public UserDuplicateException(ErrorType.Conflict errorType, String detail) {
        super(errorType, detail);
    }
}
