package com.dao.nbti.test.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "test_problem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestProblem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_problem_id")
    private int testProblemId;
    
    @Column(name = "test_result_id")
    private Integer testResultId;

    @Column(name = "problem_id")
    private int problemId;

    @Column(name = "is_correct", nullable = false)
    @Enumerated(EnumType.STRING)
    private IsCorrect isCorrect = IsCorrect.Y;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Builder
    public TestProblem(int testResultId, int problemId, IsCorrect isCorrect, String answer) {
        this.testResultId = testResultId;
        this.problemId = problemId;
        this.isCorrect = isCorrect;
        this.answer = answer;
    }

}
