package com.dao.nbti.study.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StudyResultDetailResponseDto {
    private int studyId;
    private int totalCount;
    private int correctCount;
    private List<StudyResultItem> results;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class StudyResultItem {
        private int studyResultId;
        private int problemId;
        private String contentImageUrl;
        private boolean isCorrect;
        private int level;
        private String correctAnswer;
        private String submittedAnswer;
        private String parentCategoryName;
    }
}
