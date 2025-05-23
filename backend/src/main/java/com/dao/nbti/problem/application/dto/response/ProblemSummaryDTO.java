package com.dao.nbti.problem.application.dto.response;

import com.dao.nbti.problem.domain.aggregate.AnswerTypeEnum;
import lombok.Getter;

@Getter
public class ProblemSummaryDTO {
    private int problemId;
    private Integer categoryId;
    private String parentCategoryName;
    private String childCategoryName;
    private Integer answerTypeId;
    private String answerTypeDescription;
    private int level;

    public ProblemSummaryDTO(int problemId, Integer categoryId, int level, String parentCategoryName, Integer answerTypeId, String childCategoryName) {
        this.problemId = problemId;
        this.categoryId = categoryId;
        this.level = level;
        this.parentCategoryName = parentCategoryName;
        this.childCategoryName = childCategoryName;
        this.answerTypeDescription = AnswerTypeEnum.of(answerTypeId).toString();
        this.answerTypeId = answerTypeId;
    }
}
