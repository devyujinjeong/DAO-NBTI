package com.dao.nbti.test.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryScoreDetail {
    private String categoryName;
    private String description;
    private int score;
}