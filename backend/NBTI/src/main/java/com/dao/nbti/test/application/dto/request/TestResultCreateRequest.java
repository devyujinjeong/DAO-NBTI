package com.dao.nbti.test.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class TestResultCreateRequest {

    @Min(0) @Max(6)
    private final int langComp;

    @Min(0) @Max(6)
    private final int generalKnowledge;

    @Min(0) @Max(6)

    private final int percReason;

    @Min(0) @Max(6)
    private final int workMemory;

    @Min(0) @Max(6)
    private final int procSpeed;

    @Min(0) @Max(6)
    private final int spatialPerception;

}
