package com.project.pet.global.auth;

import com.project.pet.global.auth.dto.TokenInfo;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component @Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    public final static long ACCESS_TOKEN_VALIDATION_SECOND = 60 * 20 * 1000L; // 20분
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 60 * 60 * 24 * 10 * 1000L; // 10일

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    private final String BEARER_PREFIX = "Bearer ";

    private final RedisTemplate<String, Object> redisUtil;

    @PostConstruct
    protected void init() { // base64로 인코딩하여 문자열값 secretKey에 저장
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsernameFromToken(String token) {

        String jwtToken = token.split(" ")[1].trim();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims.get("sub", String.class); // "sub" 키의 값을 추출
    }

    public long extractExpirationTimeFromToken(String token) {

        String jwtToken = token.split(" ")[1].trim();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        Date date = claims.get("exp", Date.class);
        long time = date.getTime();

        return time; // act 만료기간 long 타입으로 넘기기
    }

    public TokenInfo generateToken(Authentication authentication) { // jwt token 생성
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = createAccessToken(authentication, authorities, now);

        // Refresh Token 생성
        String refreshToken = createRefreshToken(authentication, now);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_VALIDATION_SECOND)
                .build();
    }

    private String createRefreshToken(Authentication authentication, long now) {
        String refreshToken = BEARER_PREFIX + Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(now + REFRESH_TOKEN_VALIDATION_SECOND))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    private String createAccessToken(Authentication authentication, String authorities, long now) {
        String accessToken = BEARER_PREFIX + Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(new Date(now + ACCESS_TOKEN_VALIDATION_SECOND))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    public boolean validateToken(String token) {  // 토큰의 유효성 검증을 수행
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (Exception e) {
            log.info("새로운 JWT 토큰 오류입니다.@@");
        }
        return false;
    }
    public boolean checkLogoutToken(String token){
        Boolean result = redisUtil.hasKey("AT:" + extractUsernameFromToken(token));
        return result;
    }
}