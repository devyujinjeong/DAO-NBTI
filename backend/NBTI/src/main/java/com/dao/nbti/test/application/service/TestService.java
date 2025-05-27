package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.TestSubmitRequest;
import com.dao.nbti.test.application.dto.response.TestProblemListResponse;
import com.dao.nbti.test.application.dto.response.TestResultResponse;

public interface TestService {

    // 검사 문제 제공하기
    TestProblemListResponse getTestProblems(Integer userId);

    // 검사 채점 후 저장하기
    int submitTest(TestSubmitRequest request, Integer userId);

    // 검사 결과 조회하기
    TestResultResponse getTestResult(int testResultId);

    // 검사 결과 마이페이지에 저장하기
    void updateTestResult(Integer userId, int testResultId);

}