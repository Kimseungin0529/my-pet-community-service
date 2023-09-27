package com.project.pet.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * jwt 인증 관련 필터이다. GenericFilterBean를 상속받아도 된다. GenericFilterBean는 기존 필터에서 가져올 수 없는 스프링의 설정 정보를 가져올 수 있게 확장된 추상 클래스이다.
 * 서블릿은 사용자 요청을 받으면 서블릿을 생성하여 메모리 저장하고 동일한 클라이언트 요청을 받으면 재활용
 * 하는 구조여서 GenericFilterBean를 상속받으면 RequestDispatcher에 의해 다른 서블릿으로 디스패치되면서
 * 필터가 두 번 호출되는 현상이 발생할 수 있다고 한다. 이 문제를 해결하기 위해 OncePerRequestFilter를 사용!
 * OncePerRequestFilter는 GenericFilterBean를 상속받아 구현한 필터이다.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);
        // Authorization 헤더에 있는 jwt 가져오기
        if(token != null && jwtTokenProvider.validateToken(token)){
            //토큰이 존재하면서 유효하다면 Authentication 객체 생성
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //시큐리티 컨텍스트 홀더에 Authentication 저장
        }
        filterChain.doFilter(request,response);
        //doFilter는 서블릿 실행 메서드로 해당 메소드 기준으로 서블릿 실행 전후 로직
    }
}
