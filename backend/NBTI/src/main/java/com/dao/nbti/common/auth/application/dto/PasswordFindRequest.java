package com.dao.nbti.common.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordFindRequest {
    @NotBlank
    private final String accountId;

    @NotBlank
    private final String name;
}
