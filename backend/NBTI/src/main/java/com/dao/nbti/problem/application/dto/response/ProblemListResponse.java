package com.dao.nbti.problem.application.dto.response;

import com.dao.nbti.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProblemListResponse {
    private List<ProblemSummaryDTO> problems;
    private Pagination pagination;
}
