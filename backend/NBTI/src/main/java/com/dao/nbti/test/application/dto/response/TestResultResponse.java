package com.dao.nbti.test.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TestResultResponse {
    private List<CategoryScoreDetail> scores;

    private Integer userId;
    private String aiText;
}

