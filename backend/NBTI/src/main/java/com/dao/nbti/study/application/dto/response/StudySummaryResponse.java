package com.dao.nbti.study.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudySummaryResponse {
    private int studyId;                 // 학습 ID
    private String parentCategoryName;   // 상위 분야 이름
    private LocalDateTime createdAt;     // 학습(풀이) 일시
    private int correctCount;            // 맞은 문제 수
    private int totalCount;              // 전체 문제 수
}
