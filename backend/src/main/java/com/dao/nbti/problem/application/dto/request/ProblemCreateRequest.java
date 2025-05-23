package com.dao.nbti.problem.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProblemCreateRequest implements ProblemCommandRequest {
    private int categoryId;
    private int answerTypeId;
    @NotBlank
    private String correctAnswer;
    @Min(1)
    @Max(3)
    private int level;
}
