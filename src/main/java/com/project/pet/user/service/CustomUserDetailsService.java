package com.project.pet.user.service;


import com.project.pet.user.model.Role;
import com.project.pet.user.model.User;
import com.project.pet.user.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.getTitle()));
        return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getPassword(), grantedAuthorities);
    }

    private UserDetails createUserDetails(User user) {
        return User.builder()
                .loginId(user.getLoginId())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRoles())
                .build();
    }
}
