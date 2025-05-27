package com.dao.nbti.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class UserDeleteRequest {
    @NotBlank
    private String password;
}
