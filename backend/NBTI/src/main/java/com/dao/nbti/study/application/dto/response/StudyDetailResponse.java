package com.dao.nbti.study.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class StudyDetailResponse {
    private String parentCategoryName;            // 상위 분야 이름
    private String parentCategoryDescription;     // 상위 분야 설명
    private LocalDateTime createdAt;              // 학습 일시
    private List<StudyProblemDetail> problems;    // 문제 목록
}
