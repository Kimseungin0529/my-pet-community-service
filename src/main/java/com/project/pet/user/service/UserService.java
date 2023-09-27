package com.project.pet.user.service;

import com.project.pet.auth.JwtTokenProvider;
import com.project.pet.user.dto.UserCreateRequest;
import com.project.pet.user.dto.UserLoginRequest;
import com.project.pet.user.model.User;
import com.project.pet.user.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username);
    }


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

    public void signIn(UserLoginRequest dto) {

    }
}
