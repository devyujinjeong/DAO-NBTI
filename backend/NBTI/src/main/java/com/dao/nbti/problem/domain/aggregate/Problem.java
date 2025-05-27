package com.dao.nbti.problem.domain.aggregate;

import com.dao.nbti.problem.application.dto.request.ProblemCommandRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int problemId;
    private Integer categoryId;
    private Integer answerTypeId;
    @NotBlank
    private String contentImageUrl;
    @NotBlank
    private String correctAnswer;
    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted;
    @Min(1)
    @Max(6) // 전부 상중하 하기로 했으므로 사실 3이어도 됨 (여기선 DB 체크 옵션에 맞춤)
    private int level;

    @Builder
    public Problem(Integer categoryId, Integer answerTypeId, String contentImageUrl, String correctAnswer, IsDeleted isDeleted, int level) {
        this.categoryId = categoryId;
        this.answerTypeId = answerTypeId;
        this.contentImageUrl = contentImageUrl;
        this.correctAnswer = correctAnswer;
        this.isDeleted = isDeleted;
        this.level = level;
    }

    public void updateFromRequest(ProblemCommandRequest request) {
        this.categoryId = request.getCategoryId();
        this.answerTypeId = request.getAnswerTypeId();
        this.correctAnswer = request.getCorrectAnswer();
        this.level = request.getLevel();
    }

    public void updateContentImageUrl(String newUrl) {
        this.contentImageUrl = newUrl;
    }
}
