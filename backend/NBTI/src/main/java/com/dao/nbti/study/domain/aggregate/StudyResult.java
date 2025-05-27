package com.dao.nbti.study.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studyResultId;

    private Integer studyId;

    private int problemId;

    @Enumerated(EnumType.STRING)
    private IsCorrect isCorrect;

    private String answer;
}
