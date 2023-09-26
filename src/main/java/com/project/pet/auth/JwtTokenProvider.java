package com.project.pet.auth;

import com.project.pet.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component @Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    public final static long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 60; // accessToken의 만료 시간 설정
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 120; // refreshToken의 만료 시간 설정

    @Value("${jwt.secret}") // yml(properties) 파일에서 해당 경로 값 가져오기
    private String secretKey;

    private UserService userService;



    @PostConstruct
    protected void init() { // base64로 인코딩하여 문자열값 secretKey에 저장
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        log.info("secretKey = {}", secretKey);
    }

    public String createAccessToken(String userLoginId, List<String> roles) { // jwt token 생성
        Claims claims = Jwts.claims().setSubject(userLoginId);
        claims.put("roles", roles);
        /**
         * Claim은 jwt 토근의 내용값에 해당함. 여기선 User의 LoingId(userLoginId)를 속성 값을 추가
         * roles은 사용자의 권한을 확인하기 위해 추가
         */
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() +  ACCESS_TOKEN_VALIDATION_SECOND)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();

        log.info("accessToken = {}", accessToken);
        return accessToken;
    }

    public Authentication getAuthentication(String token){ // 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
        UserDetails userDetails = userService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
    }

    public String getUsername(String token){ //secretkey를 통해 실제 username(로그인 아이디) 가져오기
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }
    
    public boolean validateToken(String token) {  // 토큰의 유효성 검증을 수행
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request){ // header 값에 Authorization(jwt) 값 가졍괴
        return request.getHeader("Authorization");
    }
}
