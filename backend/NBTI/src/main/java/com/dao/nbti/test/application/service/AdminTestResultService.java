package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.AdminTestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.AdminTestResultSummaryResponse;
import com.dao.nbti.test.application.dto.response.TestResultDetailResponse;
import org.springframework.data.domain.Pageable;

public interface AdminTestResultService {
    AdminTestResultSummaryResponse getAdminTestResultList(AdminTestResultSearchCondition condition, Pageable pageable);
    TestResultDetailResponse getTestResultDetail(int testResultId);
}
