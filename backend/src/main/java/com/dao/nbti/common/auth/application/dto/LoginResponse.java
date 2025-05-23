package com.dao.nbti.common.auth.application.dto;

import com.dao.nbti.user.domain.aggregate.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private Authority authority;
}
