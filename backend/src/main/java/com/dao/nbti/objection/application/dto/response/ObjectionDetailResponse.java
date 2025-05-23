package com.dao.nbti.objection.application.dto.response;

import com.dao.nbti.objection.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ObjectionDetailResponse {
    private int objectionId;
    private int problemId;
    private Status status;

    private String reason;           // 제출한 사유
    private String information;      // 처리 정보

    private LocalDateTime createdAt;     // 제출 일시
    private LocalDateTime processedAt;   // 처리 일시
}
