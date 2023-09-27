package com.project.pet.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
@Builder
@AllArgsConstructor // AllArgsConstructor가 과하다면 필요한 생성자에 @Builder를 달아주면 해당 생성자만 builder 기능을 사용할 수 있다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    private String email;

    @Column(nullable = false)
    private String phone;


    private Long manner;

    @Enumerated(value = EnumType.STRING) // 추후 Enum 대신 List<String> 형식으로 변환하는 것이 더 효율적으로 보임.
    private Role role;

    // 패스워드 암호화, 복화호 확인
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }
    public Boolean matchePassword(PasswordEncoder passwordEncoder, String password){
        return passwordEncoder.matches(password, getPassword());
    }


    //UserDetail 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> role.getValue()); // key: ROLE_권한
        return authorities;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
