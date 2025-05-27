package com.dao.nbti.problem.application.dto.request;

public interface ProblemCommandRequest {
    int getCategoryId();
    int getAnswerTypeId();
    String getCorrectAnswer();
    int getLevel();
}
