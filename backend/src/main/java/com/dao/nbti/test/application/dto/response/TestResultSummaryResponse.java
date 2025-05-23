package com.dao.nbti.test.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TestResultSummaryResponse {
    private int testResultId;
    private LocalDateTime createdAt;
    private String highestCategory;
    private String lowestCategory;
    private int totalScore;
}
