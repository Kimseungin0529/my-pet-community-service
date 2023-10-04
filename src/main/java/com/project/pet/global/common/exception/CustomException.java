package com.project.pet.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{

    int errorCode; // 개발자 내에서 지정한 에러코드
    ErrorType errorType; // 커스텀한 예외 enum 타입
    String detail; // 예외 메세지

    public CustomException(ErrorType errorType, String detail){
        this.errorCode = errorType.getCode();
        this.errorType = errorType;
        this.detail = detail;
    }

    /** Custom Exception 사용법
     * 주로 발생하는 에러4xx, 5xx 추상 정적 클래스로 생성하므로써 에러 종류를 크게 나눔.
     * 추상 클래스 상속을 통해 구체적인 커스템 예외를 작성하면 된다.
     * ex ) UserNotFoundException(유저 찾기 예외) extend NotFoundException
     * 특정 예외(ex : UserNotFoundException)를 만들었다면 해당하는 ErrorType(ex:USER_NOT_FOUND(4001))을 생성하여 사용하면 된다.
     * ErrorType 또한 CustomException과 같이 주로 발생하는 유형 5가지가 있고 세부적인 errorType을 작성하면 된다.
     */
    public abstract static class InvalidRequestException extends CustomException {
        public InvalidRequestException(ErrorType.BadRequest errorType, String detail) {
            super(errorType, detail);
        }
    }

    public abstract static class UnauthorizedException extends CustomException {
        public UnauthorizedException(ErrorType.Unauthorized errorType, String detail) {
            super(errorType, detail);
        }
    }

    public abstract static class ForbiddenException extends CustomException {
        public ForbiddenException(ErrorType.Forbidden errorType, String detail) {
            super(errorType, detail);
        }
    }

    public abstract static class NotFoundException extends CustomException {
        public NotFoundException(ErrorType.NotFound errorType, String detail) {
            super(errorType, detail);
        }
    }

    public abstract static class ConflictException extends CustomException {
        public ConflictException(ErrorType.Conflict errorType, String detail) {
            super(errorType, detail);
        }
    }

    public abstract static class ServerErrorException extends CustomException {
        public ServerErrorException(ErrorType errorType, String detail) {
            super(errorType, detail);
        }
    }





}
