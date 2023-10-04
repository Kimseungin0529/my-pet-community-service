package com.project.pet.global.common.exception;

import com.project.pet.global.common.dto.DtoErrorResponse;
import com.project.pet.global.common.dto.ErrorResponse;
import com.project.pet.global.common.exception.list.DtoNotValidException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class CustomExceptionHandler {

    /*
       Dto Valid Exception
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleDtoNotValid(MethodArgumentNotValidException ex) {

        // MethodArgumentNotValidException에서 FieldError들을 가져와서 메시지를 추출하여 리스트에 추가
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream( )
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.status(BAD_REQUEST).body(new DtoErrorResponse(new DtoNotValidException(), errors));
    }
    // 해당 예외처리도 필요해보임.
   /* @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(new ComoLoginFailureException()));
    }*/


    /*
   Security Exception
    */
    @ExceptionHandler(value = CustomException.InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(CustomException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(CustomException.UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(CustomException ex) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(ex));
    }
    
    @ExceptionHandler(CustomException.ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(CustomException ex) {
        return ResponseEntity.status(FORBIDDEN).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(CustomException.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotfound(CustomException ex) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(CustomException.ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(CustomException ex) {
        return ResponseEntity.status(CONFLICT).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(CustomException.ServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleServerError(CustomException ex) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex));
    }

    /**
     * 커스텀 예외가 아닌 예외 발생 시(커스텀에서 제외된 RuntimeException)에 대한 예외 처리도 필요
     */

}
