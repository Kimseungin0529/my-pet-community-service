package com.project.pet.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final String[] allowdUrls = { "", "/", "/sign-in", "/sing-up"};
    @Bean // BCryptPasswordEncoder가 제공하는 암호화로 비밀번호와 같은 중요 정보 노출을 막아준다.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // token 기반, stateless 특성으로 csrf 에 노출될 확률이 매우 낮다. 하지만 쿠키와 같은 기능을 사용한다면 따로 설정해줄 필요는 있음.
                .httpBasic(basic -> basic.disable()) // httpBasic 구조 사용 x -> authorization 헤더에 아이디와 비밀번호를 넣는 위험한 방식이 있다 정도로만 알고 가자.
                // pring Security 설정에서 httpBasic().disable()를 명시적으로 추가해주어야만 HTTP Basic Authentication이 비활성화되고, JWT 또는 다른 인증 방식을 대신 사용
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) //session, cookie를 사용하지 않으므로 stateless 설정
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(allowdUrls).permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
}
