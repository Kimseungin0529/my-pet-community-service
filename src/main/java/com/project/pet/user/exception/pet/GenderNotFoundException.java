package com.project.pet.user.exception.pet;

import com.project.pet.global.common.exception.CustomException;
import com.project.pet.global.common.exception.ErrorType;

public class GenderNotFoundException extends CustomException {

    public GenderNotFoundException(ErrorType errorType, String detail) {
        super(errorType, detail);
    }
}
