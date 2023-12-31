package com.project.pet.global.common.exception;

public interface ErrorType {
    int getCode();

    enum BadRequest implements ErrorType {
        BAD_REQUEST_DEFAULT(0),
        INVALID_REQUEST_BODY(1),
        INVALID_PARAMETER(2),
        INVALID_JWT_TOKEN(3),
        INVALID_DTO_PARAMETER(4)
        ;

        private final int errorCode;

        BadRequest(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum Unauthorized implements ErrorType {
        UNAUTHORIZED_DEFAULT(1000),
        LOGIN_UNAUTHORIZE(1001)
        ;

        private final int errorCode;

        Unauthorized(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum Forbidden implements ErrorType {
        FORBIDDEN_DEFAULT(3000)
        ;

        private final int errorCode;

        Forbidden(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum NotFound implements ErrorType {
        NOT_FOUND_DEFAULT(4000),
        USER_NOT_FOUND(4001),
        PET_SPECIES_NOT_FOUND(4002),
        PET_GENDER_NOT_FOUND(4003)
        ;

        private final int errorCode;

        NotFound(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum Conflict implements ErrorType {
        CONFLICT_DEFAULT(9000),
        USER_DUPLICATE_CONFLICT(9001)
        ;

        private final int errorCode;

        Conflict(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum ServerError implements ErrorType {
        SERVER_ERROR_DEFAULT(5000),

        FILE_UPLOAD_FAIL(5001),
        FILE_DELETE_FAIL(5002)
        ;

        private final int errorCode;

        ServerError(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }
}
