package com.dao.nbti.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminViewResponse {
    private Page<UserAdminViewDTO> page;
}
