package com.dao.nbti.test.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestResponse {

    private int problemId;
    private int categoryId;
    private String categoryName;
    private int answerTypeId;
    private String answerTypeDescription;
    private String contentImageUrl;
    private int level;
    private int timeLimit;
    private String correctAnswer;

}
