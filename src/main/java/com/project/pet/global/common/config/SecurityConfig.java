    package com.project.pet.global.common.config;

    import com.project.pet.global.auth.JwtAuthenticationFilter;
    import com.project.pet.global.auth.JwtTokenProvider;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

    @Configuration
    @EnableWebSecurity
    @RequiredArgsConstructor
    @EnableGlobalMethodSecurity(securedEnabled = true)
    public class SecurityConfig {

        private final JwtTokenProvider jwtTokenProvider;
        /** 따로 jwt security를 분리하려다가 jwtFilter만 추가하므로 통합 filter 설정으로 결정
         */

        @Bean // BCryptPasswordEncoder가 제공하는 암호화로 비밀번호와 같은 중요 정보 노출을 막아준다.
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable()) // token 기반, stateless 특성으로 csrf 에 노출될 확률이 매우 낮다. 하지만 쿠키와 같은 기능을 사용한다면 따로 설정해줄 필요는 있음.
                    .httpBasic(basic -> basic.disable()) // 로그인폼형식 -> authorization 헤더에 아이디와 비밀번호를 넣는 위험한 방식이 있다 정도로만 알고 가자.
                    // Rest API 쓰므로 사용x, HTTP Basic Authentication이 비활성화되고, JWT 또는 다른 인증 방식을 대신 사용
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS)) //session, cookie를 사용하지 않으므로 stateless 설정

                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),  UsernamePasswordAuthenticationFilter.class)
                    // JwtAuthenticationFilter를 UsernamePasswordAuthentictaionFilter 전에 적용시킨다.
                    .authorizeHttpRequests(auth ->
                            auth.requestMatchers("/api/users/sign-up").permitAll()
                                    .requestMatchers("/api/users/sign-in").permitAll()
                                    .requestMatchers("/", "").permitAll()
                                    .anyRequest().authenticated()
                    )
                    .build();
        }
    }
