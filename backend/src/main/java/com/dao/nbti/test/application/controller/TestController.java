package com.dao.nbti.test.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.test.application.dto.response.TestProblemListResponse;
import com.dao.nbti.test.application.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "검사", description = "검사 문제 조회, 결과 저장, 결과 조회")
public class TestController {

    private final TestService testService;

    /* 검사 요청하기 */
    @GetMapping("/test/problems")
    @Operation(
            summary = "지능 검사 문제 제공", description = "비회원, 회원 여부에 따라 문제를 제공합니다."
    )
    public ResponseEntity<ApiResponse<TestProblemListResponse>> getTests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer userId = (userDetails != null) ? Integer.parseInt(userDetails.getUsername()) : null;

        TestProblemListResponse problems = testService.getTestProblems(userId);

        return ResponseEntity.ok(ApiResponse.success(problems));
    }
}
