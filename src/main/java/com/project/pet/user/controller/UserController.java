package com.project.pet.user.controller;

import com.project.pet.global.auth.dto.TokenInfo;
import com.project.pet.user.dto.UserCreateRequest;
import com.project.pet.user.dto.UserLoginRequest;
import com.project.pet.user.dto.UserLogoutRequest;
import com.project.pet.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    /**
     * API 통신이므로 객체 대신 ReponseEntity를 통해 http 통신
     */

    private final UserService userService;

    @PostMapping("/sign-up") 
    public ResponseEntity<String> userSignUp(@Valid @RequestBody UserCreateRequest dto){
        userService.signUp(dto);

        return ResponseEntity.ok().body("sign-up success");
    }
    @PostMapping("/sign-in") // 로그인은 보안이 중요하므로 get 대신 post 사용(url에 정보 노출 위험 제거) + control url로 의미 명확하게 전달
    public ResponseEntity<?> userSignIn(@Valid @RequestBody UserLoginRequest dto){
        TokenInfo tokenInfo = userService.signIn(dto);

        return ResponseEntity.ok().body(tokenInfo.getAccessToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(@RequestHeader(name = "Authorization") String token){
        userService.logout(token);

        /**
         * 로그아웃 기능 수정
         * 1. rft은 주고 받지 않는다. 오직 서버 측에서만 저장한다. 이유 -> 어차피 act 탈취당한다면 rtf(body값이더라도)도 탈취당할 수 있다! 그렇기에 구현 상,
         * 비용 대비 효과가 좋지 않으므로 act이 만료됐다면 서버 측에 저장된 rft가 유효하기만 하다면 act 재발급으로 진행.
         * 보안은 다른 방법으로 추후 강화.
         */
        return ResponseEntity.ok().body("logout success");
    }

    @GetMapping("/ping") // api test 용도
    public String ping(Request request){
        String authorizaion = request.getHeader("Authorizaion");
        //contribution setting 설정 

        return "pong";
    }

}
