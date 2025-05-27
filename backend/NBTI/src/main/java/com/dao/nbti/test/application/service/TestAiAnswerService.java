package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;

public interface TestAiAnswerService {

    String createAiAnswer(TestResultCreateRequest request,  Integer userId);

}