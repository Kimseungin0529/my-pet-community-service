package com.project.pet.global.auth;

import com.project.pet.global.common.dto.ErrorResponse;
import com.project.pet.global.common.exception.ErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class LoginExceptionHandler {
    /*
    로그인 실패 예외 처리
     */
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> handlePasswordFail(BadCredentialsException ex) {

        /**
         *          모든 예외처리를 ErrorResponse를 통해 하려 했지만 형식이 다른 것도 있어 그렇게 사용하지 못하고 있다.
         *         그렇기에 CustomExceptionHandler에서 구현하지 않고 auth 패키지에 구현했다. formLoing을 사용하지 않으니 security에서 제공하는 메소드에 대한 제약을
         *         받았다. 이는 나중에 보수하자.
         */
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse( new LoginFailException() ));
    }
    // UsernameNotFoundException은 security 내부에서만 발생하는 예외이다. UsernameNotFoundException를 사용하려면
    // 따로 설정을 해줘야한다.
    /**
     * AuthenticationException의 주요 자식 예외 클래스 중 일부와 간단한 설명은 다음과 같습니다:
     *
     * BadCredentialsException: 사용자 자격 증명(예: 비밀번호)이 올바르지 않은 경우에 발생합니다.
     * CredentialsExpiredException: 사용자 자격 증명(예: 비밀번호)이 만료된 경우에 발생합니다.
     * DisabledException: 사용자 계정이 비활성화된 경우에 발생합니다.
     * LockedException: 사용자 계정이 잠겨 있는 경우에 발생합니다.
     * AccountExpiredException: 사용자 계정이 만료된 경우에 발생합니다.
     * ProviderNotFoundException: 인증 제공자(예: UserDetails를 로드하는 데 사용되는 서비스)를 찾을 수 없는 경우에 발생합니다.
     * InsufficientAuthenticationException: 사용자의 현재 인증 상태가 작업에 필요한 인증 수준에 미치지 못할 때 발생합니다.
     *
     * 해당 예외처리는 나중에 유지보수 및 확장을 할 떄 사용하자. 일단 BadCredentialsException만 구현했다.
     */
}
