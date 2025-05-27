package com.dao.nbti.objection.application.dto.request;

import com.dao.nbti.objection.domain.aggregate.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ObjectionUpdateRequest {
    @Schema(description="수정할 상태", example="ACCEPTED")
    private Status status;
    @Schema(description="처리 정보", example="지시문 수정 완료")
    private String information;
}
