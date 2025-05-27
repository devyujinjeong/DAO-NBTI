package com.dao.nbti.objection.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ObjectionCreateResponse {
    private int objectionId;
    private String message; // 예: "이의 제기가 등록되었습니다."
}
