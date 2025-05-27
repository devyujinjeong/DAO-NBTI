package com.dao.nbti.study.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitStudyRequestDto {

    @NotNull(message = "제출된 답안 리스트는 null일 수 없습니다.")
    @Valid
    private List<AnswerSubmission> answers;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerSubmission {
        @Min(value = 1, message = "problemId는 1 이상이어야 합니다.")
        private int problemId;

        @NotNull(message = "답안은 비어 있을 수 없습니다.")
        private String userAnswer;
    }
}
