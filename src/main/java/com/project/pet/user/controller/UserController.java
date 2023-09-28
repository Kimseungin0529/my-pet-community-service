package com.project.pet.user.controller;

import com.project.pet.auth.dto.TokenInfo;
import com.project.pet.user.dto.UserCreateRequest;
import com.project.pet.user.dto.UserLoginRequest;
import com.project.pet.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/ping") // api test 용도
    public String ping(Request request){
        String authorizaion = request.getHeader("Authorizaion");


        return "pong";
    }

}
