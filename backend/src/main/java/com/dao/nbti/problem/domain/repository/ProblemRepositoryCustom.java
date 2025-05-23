package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO;

import java.util.List;

public interface ProblemRepositoryCustom {

    List<ProblemSummaryDTO> getProblemsBy(ProblemSearchRequest problemSearchRequest);

    long countProblemsBy(ProblemSearchRequest problemSearchRequest);
}
