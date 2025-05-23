package com.dao.nbti.study.application.service;

import com.dao.nbti.study.application.dto.request.StudySearchCondition;
import com.dao.nbti.study.application.dto.response.StudySummaryResponse;
import com.dao.nbti.study.application.dto.response.StudyDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyResultService {
    Page<StudySummaryResponse> getStudyList(StudySearchCondition condition, Pageable pageable);
    StudyDetailResponse getStudyDetail(int studyId, int userId);
}