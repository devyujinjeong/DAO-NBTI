package com.dao.nbti.test.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "test_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestResult {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_result_id")
    private int testResultId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "lang_comp")
    private int langComp;

    @Column(name = "general_knowledge")
    private int generalKnowledge;

    @Column(name = "perc_reason")
    private int percReason;

    @Column(name = "work_memory")
    private int workMemory;

    @Column(name = "proc_speed")
    private int procSpeed;

    @Column(name = "spatial_perception")
    private int spatialPerception;

    @Column(name = "ai_text", nullable = false)
    private String aiText;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_saved")
    @Enumerated(EnumType.STRING)
    private IsSaved isSaved = IsSaved.N;

    @Builder
    public TestResult(
            Integer userId, int langComp, int generalKnowledge, int percReason, int workMemory,
            int procSpeed, int spatialPerception, String aiText, LocalDateTime createdAt, IsSaved isSaved
    ) {
        this.userId = userId;
        this.langComp = langComp;
        this.generalKnowledge = generalKnowledge;
        this.percReason = percReason;
        this.workMemory = workMemory;
        this.procSpeed = procSpeed;
        this.spatialPerception = spatialPerception;
        this.aiText = aiText;
        this.createdAt = createdAt;
        this.isSaved = isSaved != null ? isSaved : IsSaved.N;
    }

    public void saveToMyPage() {
        this.isSaved = IsSaved.Y;
    }

}
