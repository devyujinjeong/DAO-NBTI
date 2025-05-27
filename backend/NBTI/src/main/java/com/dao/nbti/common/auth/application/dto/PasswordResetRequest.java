package com.dao.nbti.common.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordResetRequest {
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{8,}$",
            message = "비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다."
    )
    private String password;
    private String verifiedPassword;
}
