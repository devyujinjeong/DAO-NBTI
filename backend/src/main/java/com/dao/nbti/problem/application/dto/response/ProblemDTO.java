package com.dao.nbti.problem.application.dto.response;

import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.Problem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProblemDTO {
    private int problemId;
    private Integer categoryId;
    private String parentCategoryName;
    private String childCategoryName;
    private Integer answerTypeId;
    private String answerTypeDescription;
    private String contentImageUrl;
    private String correctAnswer;
    private int level;

    @Builder
    public ProblemDTO(int problemId, Integer categoryId, String parentCategoryName, String childCategoryName, Integer answerTypeId, String answerTypeDescription, String contentImageUrl, String correctAnswer, int level) {
        this.problemId = problemId;
        this.categoryId = categoryId;
        this.parentCategoryName = parentCategoryName;
        this.childCategoryName = childCategoryName;
        this.answerTypeId = answerTypeId;
        this.answerTypeDescription = answerTypeDescription;
        this.contentImageUrl = contentImageUrl;
        this.correctAnswer = correctAnswer;
        this.level = level;
    }

    public static ProblemDTO from(Problem problem, Category childCategory, Category parentCategory, String answerTypeDescription) {
        return ProblemDTO.builder()
                .problemId(problem.getProblemId())
                .categoryId(problem.getCategoryId())
                .parentCategoryName(parentCategory.getName())
                .childCategoryName(childCategory.getName())
                .answerTypeId(problem.getAnswerTypeId())
                .answerTypeDescription(answerTypeDescription)
                .contentImageUrl(problem.getContentImageUrl())
                .correctAnswer(problem.getCorrectAnswer())
                .level(problem.getLevel())
                .build();
    }
}
