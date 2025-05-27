package com.dao.nbti.user.application.dto;

import com.dao.nbti.user.domain.aggregate.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Getter
@ToString
@Builder
public class UserCreateRequest {
    @NotBlank
    @Size(min = 6)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
            message = "아이디는 영문과 숫자를 각각 하나 이상 포함해야 하며, 다른 문자를 포함할 수 없습니다."
    )
    private String accountId;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{8,}$",
            message = "비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다."
    )
    private String password;

    @NotNull
    private String name;
    @NotNull
    private Gender gender;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
}
