package com.dao.nbti.study.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyProblemDetail {
    private int problemId;
    private String imageUrl;     // 문제 이미지
    private String correctAnswer;
    private String userAnswer;
    private boolean isCorrect;
    private int level;
}
