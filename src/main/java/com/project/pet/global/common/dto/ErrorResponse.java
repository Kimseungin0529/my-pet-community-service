package com.project.pet.global.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.pet.global.common.exception.CustomException;
import com.project.pet.global.common.exception.ErrorType;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Getter
public class ErrorResponse {
    private int errorCode;
    private String errorType;
    private String detail;
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY) // null이 아니고 비어있지 않는 경우만 json으로 반환
    private List<String> errors;

    public ErrorResponse(CustomException customException) {
        this.errorCode = customException.getErrorCode();
        this.errorType = customException.getErrorType().toString();
        this.detail = customException.getDetail();
    }
    public ErrorResponse(CustomException customException, List<String> errors) { // DtoNotValidException 용 글로벌 예외이므로 따로 처리
        this.errorCode = customException.getErrorCode();
        this.errorType = customException.getErrorType().toString();
        this.detail = customException.getDetail();
        this.errors = errors;
    }
}
