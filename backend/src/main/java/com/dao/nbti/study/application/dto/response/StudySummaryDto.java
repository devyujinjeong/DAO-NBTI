package com.dao.nbti.study.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StudySummaryDto {
    private int studyId;
    private int userId;
    private LocalDateTime createdAt;
    private String parentCategoryName;
    private long correctCount;
}
