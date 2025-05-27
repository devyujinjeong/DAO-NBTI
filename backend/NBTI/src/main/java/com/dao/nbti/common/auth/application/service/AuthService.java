package com.dao.nbti.common.auth.application.service;


import com.dao.nbti.common.auth.application.dto.*;
import com.dao.nbti.common.auth.domain.aggregate.RefreshToken;
import com.dao.nbti.common.auth.domain.aggregate.TempToken;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.common.jwt.JwtTokenProvider;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.dao.nbti.common.exception.ErrorCode.PASSWORD_DISCORD;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final RedisTemplate<String, TempToken> tempRedisTemplate;

    //각 계층별 메소드 명 작성 기준을 못찾아서 일단 login으로 합니다.
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByAccountIdAndDeletedAtIsNull(loginRequest.getAccountId())
                .orElseThrow(() -> new UserException(ErrorCode.INVALID_CREDENTIALS)
        );

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new UserException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getAuthority().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getUserId()), user.getAuthority().name());

        RefreshToken redisRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .build();

        redisTemplate.opsForValue().set(
                String.valueOf(user.getUserId()),
                redisRefreshToken,
                Duration.ofDays(7)
        );

        log.info("{} + 가 로그인하였습니다.", user.getUserId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authority(user.getAuthority())
                .build();
    }

    public TokenResponse refreshToken(String providedRefreshToken) {
        // 리프레시 토큰 유효성 검사, 저장 되어 있는 userId 추출
        jwtTokenProvider.validateToken(providedRefreshToken);
        String userId = jwtTokenProvider.getUsernameFromJWT(providedRefreshToken);

        // Redis에 저장된 리프레시 토큰 조회
        RefreshToken storedRefreshToken = redisTemplate.opsForValue().get(userId);
        if (storedRefreshToken == null) {
            throw new BadCredentialsException("해당 유저로 조회되는 리프레시 토큰 없음");
        }

        // 넘어온 리프레시 토큰과 Redis의 토큰 비교
        if (!storedRefreshToken.getToken().equals(providedRefreshToken)) {
            throw new BadCredentialsException("리프레시 토큰 일치하지 않음");
        }

        User user = userRepository.findByUserIdAndDeletedAtIsNull(Integer.valueOf(userId))
                .orElseThrow(() -> new BadCredentialsException("해당 리프레시 토큰을 위한 유저 없음"));

        // 새로운 토큰 재발급
        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getAuthority().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getUserId()), user.getAuthority().name());


        RefreshToken newToken = RefreshToken.builder()
                .token(refreshToken)
                .build();

        // Redis에 새로운 리프레시 토큰 저장 (기존 토큰 덮어쓰기)
        redisTemplate.opsForValue().set(
                String.valueOf(user.getUserId()),
                newToken,
                Duration.ofDays(7)
        );

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        if(refreshToken == null)
            return;
        // refresh token의 서명 및 만료 검증
        jwtTokenProvider.validateToken(refreshToken);
        String userId = jwtTokenProvider.getUsernameFromJWT(refreshToken);
        redisTemplate.delete(userId);
        log.info("{} + 가 로그아웃하였습니다!", userId);
    }

    @Transactional
    public TokenResponse findPassword(PasswordFindRequest request) {
        String accountId = request.getAccountId();
        String name = request.getName();
        User user = userRepository.findByAccountIdAndDeletedAtIsNullAndName(accountId,name).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );

        String token = jwtTokenProvider.createToken(String.valueOf(user.getUserId()),null);
        TempToken tempToken = TempToken.builder()
                    .token(token)
                    .build();

        tempRedisTemplate.opsForValue().set(
                "temp "+user.getUserId(),
                    tempToken,
                Duration.ofMinutes(5)
        );

        return TokenResponse.builder()
                .accessToken(token)
                .refreshToken(null)
                .build();
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request,String userIdStr) {
        String password = request.getPassword();
        String verifiedPassword = request.getVerifiedPassword();
        if(!password.equals(verifiedPassword)){
            throw new UserException(PASSWORD_DISCORD);
        }
        log.info("2");

        TempToken tempToken = tempRedisTemplate.opsForValue().get("temp "+userIdStr);
        if (tempToken == null) {
            throw new BadCredentialsException("해당 유저로 조회되는 토큰 없음");
        }
        tempRedisTemplate.delete("temp " + userIdStr);

        User user = userRepository.findByUserIdAndDeletedAtIsNull(Integer.parseInt(userIdStr)).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
