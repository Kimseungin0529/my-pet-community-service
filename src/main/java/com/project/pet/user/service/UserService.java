package com.project.pet.user.service;

import com.project.pet.global.auth.JwtTokenProvider;
import com.project.pet.global.auth.dto.ReissueToken;
import com.project.pet.global.auth.dto.TokenInfo;
import com.project.pet.user.dto.pet.PetRequset;
import com.project.pet.user.dto.user.UserCreateRequest;
import com.project.pet.user.dto.user.UserLoginRequest;
import com.project.pet.user.exception.UserDuplicateException;
import com.project.pet.user.exception.UserNotFoundException;
import com.project.pet.user.model.User;
import com.project.pet.user.model.pet.Gender;
import com.project.pet.user.model.pet.Pet;
import com.project.pet.user.model.pet.Species;
import com.project.pet.user.repositoy.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.project.pet.global.common.exception.ErrorType.Conflict.USER_DUPLICATE_CONFLICT;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplete;



    public void signUp(UserCreateRequest dto) {
        if( checkDuplicateLoginId(dto.getLoginId()) ){
            throw new UserDuplicateException(USER_DUPLICATE_CONFLICT, "해당 아이디는 이미 존재합니다.");
        }
        if( checkDuplicateNickname(dto.getNickname()) ){
            throw new UserDuplicateException(USER_DUPLICATE_CONFLICT, "해당 닉네임은 이미 존재합니다.");
        }

        User user = User.builder()
                .nickname(dto.getNickname())
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
        log.info("UsernamePasswordAuthenticationToken = {}", authenticationToken);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication = {}", authentication);

        // 로그아웃하는 경우 해당 act를 통해 "AT:사용자아이디"로 redis에 저장했음(블랙리스트). -> 인가 권한을 막기 위해
        // 로그인 성공 시, 블랙리스트에 키 값이 있다면 해당 정보를 삭제해주자. (JwtAuthenticationFilter에서 "AT:아이디" key 가 존재하면 접근 불가로 지정)
        checkActBlackList(authentication);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication); // 토큰 정보 발급
        /**
         * redis에 refresh token 저장! como에서는 jpa data를 활용한 repository를 만들어 저장했지만 여기서는
         * redisTemplate이 제공하는 set 메소드를 통해 직접 저장. 각 방법에 장단점이 있음. 여기서는 빠른 사용을 위해 해당 방법 채택
         */
        redisTemplete.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return tokenInfo;
    }

    private void checkActBlackList(Authentication authentication) {
        if(redisTemplete.hasKey("AT:" + authentication.getName())){
            redisTemplete.delete("AT:" + authentication.getName());
        }
    }

    public void logout(String token) {
        String findUserId = jwtTokenProvider.extractUsernameFromToken(token);
        if( redisTemplete.opsForValue().get("RT:" + findUserId) != null){
            //act 블랙리스트 저장 -> act 저장 - ("AT:" + name, act, act 만료기간, TimeUnit.MILLISECONDS) /
            long exp = jwtTokenProvider.extractExpirationTimeFromToken(token);

            // logout -> act 블랙리스트 저장, rft 삭제
            redisTemplete.opsForValue().set("AT:" + findUserId, token, exp, TimeUnit.MILLISECONDS);
            redisTemplete.delete("RT:" + findUserId);
        }
        else{
            /**
             * 인증되지 않은 사용자라는 예외처리 필요
             */
        }
    }
    /**
     * act 만료로 인해 재발급이 필요한 상황
     * 1. rft이 존재한다면 act을 재발급
     * 2. rtf이 존재하지 않는다면 로그인 필요
     *  예외처리 -> rtf 존재x, 로그인 필요
     */
    public String reissue(ReissueToken token) {
        String result = null;
        String originRefreshToken = token.getRefreshToken();
        String resolvedToken = token.getRefreshToken().substring(7);

        Claims claims = jwtTokenProvider.parseClaims(resolvedToken);
        String loginId = (String)claims.get("sub");
        String redisRft = (String) redisTemplete.opsForValue().get("RT:" + loginId); // 존재하지 않는다면 null 반환

        //클린 코드인가를 묻냐면 아닌 거 같다. 내 수준이 매우 낮으니 버그 발생이 없는 것을 목표로 빠르게 작성하고 리팩토링은 추후에 하자.
        // 아직 남아있는 redisRft가 있다면 redisRft와  rft를 비교, 사실 보안적으로 큰 향상은 없어 보이나 안 한 것보다 나으므로 적용
        if( redisTemplete.opsForValue().get("RT:" + loginId) != null && redisRft.equals(originRefreshToken)){
            long now = new Date().getTime();
            String reissueAct = jwtTokenProvider.reissueAccessToken(loginId, now);

            result = reissueAct;
        }
        if(result == null){
            throw new IllegalArgumentException("저장된 rtf이 없거나 일치하지 않습니다.");
        }
        return result;
    }
    /** 재발급 서비스 로직 문제점
     *
     * 사실 이 재발급 과정은 매우 좋지 않다. jwtProvider이 제공하는 createAccessToken()이 있는데도 파라미터 변수가 매칭이 되지 않아
     * 같은 기능의 코드를 파라미터만 바꿔 다시 만들었다. 마치 오버라이딩과 같이 사용했지만 중복된 코드이므로 가독성을 떨어뜨린다.
     * 또한 형식도 Authentication을 통해 사용자 아이디와 역할(Role)을 얻는 것이 아니라 직접 아이디 값을 넣어주고
     * reissueAccessToken() 내부에서 "일반 사용자"라고 고정하여 재생성한다.
     * 만약 "일반 사용자"가 아닌 "관리자"인 경우에 의도치 않은 버그가 발생한다.
     *
     * 그런데도 이렇게 작성한 이유는? 완벽하게 하나씩 지식을 나의 것으로 확장시키는 것이 맞다고 생각하지만 조바심 떄문인지
     * 빠르게 프로젝트를 완성시키고 리팩토링하려고 한다. 구글링으로는 원하는 지식을 빠르게 습득하기 어렵다고 판단하여(구글링 기술부족같음)
     * 추후 강의 또는 세션을 통해 빠르게 리팩토링하겠다.
     */

    public Boolean checkDuplicateLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }

    public Boolean checkDuplicateNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }


    /*
       pet
    */
    public void settingPet(String loginId, PetRequset petRequset) {
        User findUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new UserNotFoundException());
        String name = petRequset.getName();
        Species species = Species.of(petRequset.getSpecies());
        Gender gender = Gender.of(petRequset.getGender());

        Pet pet = Pet.builder()
                .name(name)
                .species(species)
                .gender(gender)
                .build();

        findUser.settingPet(pet);
    }
}














