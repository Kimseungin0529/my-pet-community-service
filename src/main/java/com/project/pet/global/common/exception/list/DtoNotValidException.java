package com.project.pet.global.common.exception.list;

import com.project.pet.global.common.exception.CustomException;
import com.project.pet.global.common.exception.ErrorType;

/**
 * 글로벌 예외이므로 따로 common에 만듦.
 */
public class DtoNotValidException extends CustomException.InvalidRequestException {
    public DtoNotValidException() {
        super(ErrorType.BadRequest.INVALID_DTO_PARAMETER, "입력한 정보를 다시 확인해주세요.");
        System.out.println("너가 문제냐?");
    }
}
