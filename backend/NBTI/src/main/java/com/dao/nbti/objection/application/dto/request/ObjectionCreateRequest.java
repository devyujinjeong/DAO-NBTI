package com.dao.nbti.objection.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
public class ObjectionCreateRequest {

    @NotNull(message = "문제 ID는 필수입니다.")
    private Integer problemId;

    @NotBlank(message = "이의 제기 사유를 입력해주세요.")
    private String reason;
}
