package com.dao.nbti.common.auth.application.service;

import com.dao.nbti.common.auth.application.dto.LoginRequest;
import com.dao.nbti.common.auth.application.dto.LoginResponse;
import com.dao.nbti.common.auth.application.dto.TokenResponse;
import com.dao.nbti.common.auth.domain.aggregate.RefreshToken;
import com.dao.nbti.common.jwt.JwtTokenProvider;
import com.dao.nbti.user.domain.aggregate.Authority;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisTemplate<String, RefreshToken> redisTemplate;
    @InjectMocks
    private AuthService authService;

    private User user;
    private final int userId = 1;
    private final String accountId = "userId0001";
    private final String password = "encodedPassword";
    private final String accessToken = "accessToken";
    private final String refreshToken = "refreshToken";
    private final Authority authority = Authority.USER;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(userId)
                .accountId(accountId)
                .password(password)
                .authority(authority)
                .build();
    }

    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest(accountId,password);
        ValueOperations<String, RefreshToken> valueOperations = mock(ValueOperations.class);

        when(userRepository.findByAccountIdAndDeletedAtIsNull(loginRequest.getAccountId())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createToken(any(), any())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(any(), any())).thenReturn(refreshToken);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        LoginResponse loginResponse = authService.login(loginRequest);

        assertEquals(accessToken,loginResponse.getAccessToken());
        assertEquals(refreshToken, loginResponse.getRefreshToken());
        assertEquals(authority, loginResponse.getAuthority());
    }

    @Test
    void login_password_discord(){
        LoginRequest loginRequest = new LoginRequest(accountId, "discord");

        when(userRepository.findByAccountIdAndDeletedAtIsNull(accountId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())).thenReturn(false);

        assertThrows(UserException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_accountId_discord(){
        LoginRequest loginRequest = new LoginRequest("discord",password);
        when(userRepository.findByAccountIdAndDeletedAtIsNull(loginRequest.getAccountId())).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> authService.login(loginRequest));
    }


    @Test
    void refreshToken() {
        String providedRefreshToken = "refreshToken";
        RefreshToken storedRefreshToken = RefreshToken.builder()
                        .token(refreshToken)
                        .build();
        ValueOperations<String, RefreshToken> valueOperations = mock(ValueOperations.class);

        when(jwtTokenProvider.validateToken(providedRefreshToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(providedRefreshToken)).thenReturn(String.valueOf(userId));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(String.valueOf(userId))).thenReturn(storedRefreshToken);
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.createToken(any(), any())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(String.valueOf(userId), user.getAuthority().name())).thenReturn(refreshToken);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        TokenResponse tokenResponse =  authService.refreshToken(providedRefreshToken);
        assertEquals(tokenResponse.getAccessToken(), accessToken);
        assertEquals(tokenResponse.getRefreshToken(),refreshToken);
    }

    @Test
    void refreshToken_not_found(){
        String providedRefreshToken = "refreshToken";
        ValueOperations<String, RefreshToken> valueOperations = mock(ValueOperations.class);
        String unknownId = "unknown";

        when(jwtTokenProvider.validateToken(providedRefreshToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(providedRefreshToken)).thenReturn(unknownId);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(unknownId)).thenReturn(null);

        assertThrows(BadCredentialsException.class, () ->authService.refreshToken(providedRefreshToken));
    }

    @Test
    void refreshToken_token_discord(){
        String providedRefreshToken = "refreshToken";
        ValueOperations<String, RefreshToken> valueOperations = mock(ValueOperations.class);
        RefreshToken storedRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .build();
        String discordToken = "discord";
        RefreshToken discordRefreshToken = RefreshToken.builder()
                .token(discordToken)
                .build();

        when(jwtTokenProvider.validateToken(providedRefreshToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(providedRefreshToken)).thenReturn(String.valueOf(userId));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(String.valueOf(userId))).thenReturn(discordRefreshToken);


        assertThrows(BadCredentialsException.class, () ->authService.refreshToken(providedRefreshToken));
    }

    @Test
    void logout(){
        String refreshToken = "refreshToken";
        when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJWT(refreshToken)).thenReturn(String.valueOf(userId));
        when(redisTemplate.delete(String.valueOf(userId))).thenReturn(true);

        authService.logout(refreshToken);
    }


}