package com.dao.nbti.common.auth.application.controller;

import com.dao.nbti.common.auth.application.dto.*;
import com.dao.nbti.common.auth.application.service.AuthService;
import com.dao.nbti.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @Operation(summary = "로그인", description = "accountId와 password을 입력받아 로그인하고 토큰을 발급받는다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.login(loginRequest);
        ResponseCookie cookie = createRefreshTokenCookie(response.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(response));
    }


    @Operation(summary = "토큰 재발급", description = "만료된 토큰을 재발급받아 재로그인 없이 인증 상태를 유지한다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // refreshToken이 없으면 401 반환
        }
        TokenResponse tokenResponse = authService.refreshToken(refreshToken);
        ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());  // refreshToken 쿠키 생성
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(tokenResponse));
    }

    @Operation(summary = "로그아웃", description = "사용자는 인증 토큰을 삭제하며 로그아웃한다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {

        authService.logout(refreshToken);

        ResponseCookie deleteCookie = createDeleteRefreshTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "비밀번호 찾기", description = "사용자는 아이디와 이름을 입력하여 비밀번호 변경을 요청한다.")
    @PostMapping("/find-password")
    public ResponseEntity<ApiResponse<TokenResponse>> requestPasswordReset(@RequestBody PasswordFindRequest request){
        TokenResponse response = authService.findPassword(request);

        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "비밀번호 재설정", description = "사용자의 이름과 아이디 정보가 일치할 시 비밀번호를 재설정한다.")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid PasswordResetRequest request, @AuthenticationPrincipal UserDetails userDetails){
        authService.resetPassword(request, userDetails.getUsername());

        return ResponseEntity.ok().body(ApiResponse.success(null));
    }


    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                // .secure(true) // 운영 환경에서 HTTPS 사용 시 활성화
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
    }

    private ResponseCookie createDeleteRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                // .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
