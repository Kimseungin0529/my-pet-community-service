package com.project.pet.user.exception.pet;

import com.project.pet.global.common.exception.CustomException;
import com.project.pet.global.common.exception.ErrorType;

public class SpeciesNotFoundException extends CustomException.NotFoundException {

    public SpeciesNotFoundException(ErrorType.NotFound errorType, String detail) {
        super(errorType, detail);
    }
}
