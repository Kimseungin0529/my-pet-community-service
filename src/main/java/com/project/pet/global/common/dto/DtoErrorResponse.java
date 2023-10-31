package com.project.pet.global.common.dto;

import com.project.pet.global.common.exception.CustomException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
public class DtoErrorResponse {
    private int errorCode;
    private String errorType;
    private String detail;
    private List<String> errors;

    public DtoErrorResponse(CustomException customException, List<String> errorList) {
        this.errorCode = customException.getErrorCode();
        this.errorType = customException.getErrorType().toString();
        this.detail = customException.getDetail();
        this.errors = errorList;
    }
}
