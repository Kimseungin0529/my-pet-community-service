package com.project.pet.user.service;

import com.project.pet.global.auth.JwtTokenProvider;
import com.project.pet.global.auth.dto.TokenInfo;
import com.project.pet.user.dto.UserCreateRequest;
import com.project.pet.user.dto.UserLoginRequest;
import com.project.pet.user.model.User;
import com.project.pet.user.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;



    public void signUp(UserCreateRequest dto) {
        User user = User.builder()
                .name(dto.getName())
                .loginId(dto.getLoginId())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
        user.encodePassword(passwordEncoder); // security가 제공한 기능을 통해 패스워드 암호화 / 같은 문자열이라도 encode할 때마다 값이 달라지므로 유추가 어렵단 장점

        userRepository.save(user);
    }

    public TokenInfo signIn(UserLoginRequest dto) {

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getLoginId(),dto.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication); // 토큰 정보 발급

        /**
         * redis에 refresh token 저장! como에서는 jpa data를 활용한 repository를 만들어 저장했지만 여기서는
         * redisTemplate이 제공하는 set 메소드를 통해 직접 저장. 각 방법에 장단점이 있음. 여기서는 빠른 사용을 위해 해당 방법 채택
         */
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return tokenInfo;
    }
}
