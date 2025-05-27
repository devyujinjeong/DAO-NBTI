package com.dao.nbti.objection.application.dto.response;

import com.dao.nbti.objection.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminObjectionDTO {
    private Integer userId;
    private String accountId;
    private int objectionId;
    private int problemId;
    private Status status;           // 상태: PENDING / ACCEPTED / REJECTED
    private LocalDateTime createdAt; // 제출 일시

    public AdminObjectionDTO(Integer userId, String accountId, int objectionId, int problemId, Status status, LocalDateTime createdAt) {
        this.userId = userId;
        this.accountId = accountId;
        this.objectionId = objectionId;
        this.problemId = problemId;
        this.status = status;
        this.createdAt = createdAt;
    }
}
