package com.dao.nbti.objection.application.dto.response;

import com.dao.nbti.objection.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ObjectionSummaryResponse {
    private int objectionId;
    private int problemId;
    private String parentCategoryName;
    private String reason;
    private Status status;           // 상태: PENDING / ACCEPTED / REJECTED
    private LocalDateTime createdAt; // 제출 일시
}
