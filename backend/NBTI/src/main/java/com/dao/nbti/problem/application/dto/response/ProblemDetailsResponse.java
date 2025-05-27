package com.dao.nbti.problem.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemDetailsResponse {
    private ProblemDTO problem;
}
