package com.dao.nbti.problem.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProblemUpdateRequest implements ProblemCommandRequest {
    @Schema(description="수정할 하위 분야 ID", example="7")
    private int categoryId;
    @Schema(description="수정할 답안 유형 ID", example="2")
    private int answerTypeId;
    @NotBlank
    @Schema(description="수정할 정답", example="2")
    private String correctAnswer;
    @Min(1)
    @Max(3)
    @Schema(description="수정할 난이도", example="1")
    private int level;
}
