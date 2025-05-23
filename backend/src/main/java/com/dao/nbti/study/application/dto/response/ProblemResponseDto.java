package com.dao.nbti.study.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemResponseDto {
    private int problemId;
    private int categoryId;
    private String categoryName;
    private int answerTypeId;
    private String correctAnswer;
    private String answerTypeDescription;
    private String contentImageUrl;
    private int level;
    private int timeLimit;
}
