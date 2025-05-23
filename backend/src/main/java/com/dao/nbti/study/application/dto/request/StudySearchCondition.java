package com.dao.nbti.study.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudySearchCondition {
    private Integer year;              // 조회 연도 (nullable)
    private Integer month;             // 조회 월 (nullable, 1~12)
    private Integer parentCategoryId;  // 상위 카테고리 ID (nullable)
    private int userId;                // 로그인한 사용자 ID
}