package com.dao.nbti.study.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.study.application.dto.request.StudySearchRequestDto;
import com.dao.nbti.study.application.dto.response.StudySummaryListResponseDto;
import com.dao.nbti.study.application.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 - 학습 조회", description = "학습 목록, 세부 내역 조회")
public class AdminStudyController {

    private final StudyService studyService;

    @GetMapping("/study")
    @Operation(summary = "관리자 학습 내역 조회", description = "모든 학습 내역에 대해 상위 카테고리, 기간, 회원 ID 필터를 통해 조회")
    public ResponseEntity<ApiResponse<StudySummaryListResponseDto>> getStudyCategoryList(
            @ModelAttribute @Valid StudySearchRequestDto request
    ) {
        StudySummaryListResponseDto response = studyService.getStudySummaries(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
