package com.project.pet.user;

import com.google.gson.Gson;
import com.project.pet.user.controller.UserController;
import com.project.pet.user.dto.user.UserCreateRequest;
import com.project.pet.user.dto.user.UserLoginRequest;
import com.project.pet.user.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


/**
 * Mockito는 개발자가 동작을 직접 제어할 수 있는 가짜 객체를 지원하는 테스트 프레임워크이다. 일반적으로 Spring으로 웹 애플리케이션을 개발하면,
 * 여러 객체들 간의 의존성이 생긴다. 이러한 의존성은 단위 테스트를 작성을 어렵게 하는데, 이를 해결하기 위해 가짜 객체를 주입시켜주는 Mockito 라이브러리를 활용할 수 있다.
 * Mockito를 활용하면 가짜 객체에 원하는 결과를 Stub하여 단위 테스트를 진행할 수 있다. 물론 프레임워크 도구가 필요없다면 사용하지 않는 것이 가장 좋다.
 *
 *
 * Mockito에서 가짜 객체의 의존성 주입을 위해서는 크게 3가지 어노테이션이 사용된다.
 *
 * @Mock: 가짜 객체를 만들어 반환해주는 어노테이션
 * @Spy: Stub하지 않은 메소드들은 원본 메소드 그대로 사용하는 어노테이션
 * @InjectMocks: @Mock 또는 @Spy로 생성된 가짜 객체를 자동으로 주입시켜주는 어노테이션
 *
 * 예를 들어 UserController에 대한 단위 테스트를 작성하고자 할 때, UserService를 사용하고 있다면 @Mock 어노테이션을 통해 가짜 UserService를 만들고,
 * @InjectMocks를 통해 UserController에 이를 주입시킬 수 있다.
 */
@ExtendWith(MockitoExtension.class) // junit5부터 해당 어노테이션을 통해 mockito 사용
public class UserControllerTest {
    @InjectMocks // @Mock 또는 @Spy을 통해 생성된 가짜 객체를 자동으로 주입
    UserController userController;
    @Mock // 가짜 객체 생성하여 반환
    UserService userService;

    MockMvc mockMvc; // controller test를 위해선 http 호출 필요 -> MockMvc가 이를 제공.

    @BeforeEach // test 시작 전, mockmvc 생성 및 초기화
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("회원 가입 성공")
    void 회원가입성공() throws Exception{
        //given
        UserCreateRequest userCreateRequest = userCreateRequest();
        //String response = "sign-up success";
        doNothing()
                .when(userService)
                .signUp(any(UserCreateRequest.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(userCreateRequest))
        );

        //then
        System.out.println("resultActions = "  +resultActions.toString() );
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("loginId").exists())
                .andExpect(jsonPath("password").exists())
                .andExpect(jsonPath("checkedPassword").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("phone").exists())
                .andExpect(jsonPath("nickname").exists());

    }
    private UserCreateRequest userCreateRequest(){
        return UserCreateRequest.builder()
                .loginId("tmdwls")
                .password("rla363636!")
                .checkedPassword("rla363636!")
                .email("whffkaos007@naver.com")
                .phone("010-1234-1234")
                .nickname("구루루")
                .build();
    }

    @Test
    @DisplayName("로그인 성공")
    void 로그인성공() throws Exception {
        //given
        UserLoginRequest request = userLoginRequest();
        String token = "Bearer dfasdfdsafasdfasdfasdfsdfasdfasdfs.dfasdfsadfsadfawedasfsadf.fasgaraharehgr";
        doReturn(token)
                .when(userService)
                .signIn(any(UserLoginRequest.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/users/sign-in")
                        .content(new Gson().toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        // accessToken이 존재하는지("Bearer "로 시작하는지)
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(Matchers.startsWith("Bearer ")))
                .andDo(MockMvcResultHandlers.print());

    }

    private UserLoginRequest userLoginRequest() {
        return UserLoginRequest.builder()
                .loginId("tmdwls")
                .password("rla363636!")
                .build();
    }

    @Test
    @DisplayName("로그아웃 성공")
    void 로그아웃(){

    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void 토큰_재발급(){

    }

    @Test
    @DisplayName("중복된 아이디입니다")
    void 아이디_중복체크(){

    }

    @Test
    @DisplayName("중복된 닉네임입니다")
    void 닉네임_중복체크(){

    }
}
