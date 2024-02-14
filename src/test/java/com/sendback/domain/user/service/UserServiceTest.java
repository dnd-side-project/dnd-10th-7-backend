package com.sendback.domain.user.service;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.ServiceTest;
import com.sendback.global.config.jwt.JwtProvider;
import com.sendback.global.exception.type.SignInException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static com.sendback.domain.user.exception.UserExceptionType.INVALID_SIGN_TOKEN;
import static com.sendback.domain.user.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class UserServiceTest extends ServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("사용자 추가정보를 통해 회원가입을 진행한다. 성공하면 200과 함께 access token, refresh token을 반환한다.")
    public void signUpUser_success() throws Exception {
        // given
        String signToken = "valid signToken";
        doNothing().when(jwtProvider).validateSignToken(any());
        given(jwtProvider.getSignUserInfo(signToken)).willReturn(mock_signingAccount);
        given(userRepository.save(any(User.class))).willReturn(mock_user);
        given(jwtProvider.issueToken(mock_user.getId()))
                .willReturn(new Token("valid accessToken", "valid refreshToken"));

        // when
        Token resultToken = userService.signUpUser(mock_signUpRequestDto);

        // then
        verify(jwtProvider).validateSignToken(any());
        verify(jwtProvider).getSignUserInfo(signToken);
        verify(userRepository).save(any(User.class));
        verify(jwtProvider).issueToken(mock_user.getId());
        assertNotNull(resultToken);
        assertEquals("valid accessToken", resultToken.accessToken());
        assertEquals("valid refreshToken", resultToken.refreshToken());
    }

    @Test
    @DisplayName("올바르지 않은 sign token으로 회원가입 시 예외 발생")
    public void signUpUser_invalidSignToken_throwsException() {
        // given
        String signToken = "Invalid signToken";
        doThrow(new SignInException(INVALID_SIGN_TOKEN))
                .when(jwtProvider).validateSignToken(signToken);

        // when, then
        assertThatThrownBy(() -> userService.signUpUser(mock_Invalid_SignToken_signUpRequestDto))
                .isInstanceOf(SignInException.class)
                .hasMessage(INVALID_SIGN_TOKEN.getMessage());
    }
}
