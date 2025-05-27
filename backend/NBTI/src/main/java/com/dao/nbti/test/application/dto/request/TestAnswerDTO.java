package com.dao.nbti.test.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class TestAnswerDTO {

    @Min(value = 1, message = "문제 아이디는 1 이상이어야 합니다.")
    private int problemId;

    private String answer;
}

