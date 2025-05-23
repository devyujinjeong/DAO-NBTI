package com.dao.nbti.objection.application.dto.response;

import com.dao.nbti.objection.domain.aggregate.Objection;
import com.dao.nbti.objection.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminObjectionDetailsDTO {
    private int objectionId;
    private Integer userId;
    private String accountId;
    private int problemId;
    private Status status;

    private String reason;           // 제출한 사유
    private String information;      // 처리 정보

    private LocalDateTime createdAt;     // 제출 일시
    private LocalDateTime processedAt;   // 처리 일시

    @Builder
    public AdminObjectionDetailsDTO(Integer userId, String accountId, int objectionId, int problemId, Status status, String reason, String information, LocalDateTime createdAt, LocalDateTime processedAt) {
        this.userId = userId;
        this.accountId = accountId;
        this.objectionId = objectionId;
        this.problemId = problemId;
        this.status = status;
        this.reason = reason;
        this.information = information;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    public static AdminObjectionDetailsDTO from(Objection objection, String accountId) {
        return AdminObjectionDetailsDTO.builder()
                .objectionId(objection.getObjectionId())
                .userId(objection.getUserId())
                .accountId(accountId)
                .problemId(objection.getProblemId())
                .reason(objection.getReason())
                .information(objection.getInformation())
                .status(objection.getStatus())
                .createdAt(objection.getCreatedAt())
                .processedAt(objection.getProcessedAt())
                .build();
    }
}
