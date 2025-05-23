package com.dao.nbti.study.domain.repository;

import com.dao.nbti.study.application.dto.request.StudySearchRequestDto;
import com.dao.nbti.study.application.dto.response.StudySummaryDto;

import java.util.List;

public interface StudyRepositoryCustom {
    List<StudySummaryDto> getStudySummaries(StudySearchRequestDto request);
    long countStudySummaries(StudySearchRequestDto request);
}
