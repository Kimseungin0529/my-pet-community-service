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

    public ErrorResponse(CustomException customException) {
        this.errorCode = customException.getErrorCode();
        this.errorType = customException.getErrorType().toString();
        this.detail = customException.getDetail();
    }
}
