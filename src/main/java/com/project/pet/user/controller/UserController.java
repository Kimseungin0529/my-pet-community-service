package com.project.pet.user.controller;

import com.project.pet.global.auth.UserAthentication;
import com.project.pet.global.auth.dto.ReissueToken;
import com.project.pet.global.auth.dto.TokenInfo;
import com.project.pet.user.dto.pet.PetRequset;
import com.project.pet.user.dto.user.UserCreateRequest;
import com.project.pet.user.dto.user.UserLoginRequest;
import com.project.pet.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<TokenInfo> userSignIn(@Valid @RequestBody UserLoginRequest dto){
        TokenInfo tokenInfo = userService.signIn(dto);

        return ResponseEntity.ok().body(tokenInfo);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(@RequestHeader(name = "Authorization") String token){
        userService.logout(token);

        return ResponseEntity.ok().body("logout success");
    }

    @GetMapping("/reissue")
    public ResponseEntity<String> userReissueAccessToken(@RequestBody ReissueToken token){
        String reissue = userService.reissue(token);

        return ResponseEntity.ok().body(reissue);
    }

    @GetMapping("/duplicate/login-id/{id}")
    public ResponseEntity<String> loginIdDuplicate(@PathVariable("id") String loginId){
        Boolean result = userService.checkDuplicateLoginId(loginId);
        System.out.println("result = " + result);
        if(result){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 id입니다. 다른 id를 사용해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 id입니다.");
    }

    @GetMapping("/duplicate/nickname/{nickname}")
    public ResponseEntity<String> nameDuplicate(@PathVariable("nickname") String nickname){
        Boolean result = userService.checkDuplicateNickname(nickname);
        if(result){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 nickname입니다. 다른 nickname을 사용해주세요.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 nickname입니다.");
        /**
         * 아이디, 닉네임 중복 체크 API를 따로 만듦. 여기는 예외 처리 x / 회원가입하는 과정에서 따로 중복 예외처리해줌.
         */
    }

    @GetMapping("/ping") // api test 용도
    public String ping(Request request){
        String authorizaion = request.getHeader("Authorizaion");
        //contribution setting 설정 
        return "pong";
    }

    // Pet
    @PostMapping("/pet")
    public ResponseEntity<?> userSettingPet(@UserAthentication String loginId,
                                            @RequestBody PetRequset petRequset){
        userService.settingPet(loginId, petRequset);
        return ResponseEntity.ok().body("settingPet success");
    }

}
