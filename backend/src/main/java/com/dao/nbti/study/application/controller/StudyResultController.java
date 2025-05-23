package com.dao.nbti.study.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.study.application.dto.request.StudySearchCondition;
import com.dao.nbti.study.application.dto.response.StudyDetailResponse;
import com.dao.nbti.study.application.dto.response.StudySummaryResponse;
import com.dao.nbti.study.application.service.StudyResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/studies")
@Tag(name = "학습 내역", description = "사용자 학습 내역 조회 API")
public class StudyResultController {

    private final StudyResultService studyResultService;

    @Operation(summary = "학습 내역 목록 조회", description = "로그인한 사용자의 학습 이력을 조회합니다. 연도, 월, 상위 분야 필터 및 페이지네이션을 지원합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudyList(
            @Parameter(hidden = true, description = "JWT 인증된 사용자 정보")
            @AuthenticationPrincipal UserDetails userDetails,

            @Parameter(description = "조회할 연도", example = "2025")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "조회할 월 (1~12)", example = "5")
            @RequestParam(required = false) Integer month,

            @Parameter(description = "상위 카테고리 ID", example = "1")
            @RequestParam(required = false) Integer parentCategoryId,

            @Parameter(description = "페이지 및 정렬 정보")
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        StudySearchCondition condition = new StudySearchCondition(year, month, parentCategoryId, userId);
        Page<StudySummaryResponse> resultPage = studyResultService.getStudyList(condition, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", resultPage.getContent());
        response.put("pagination", Pagination.builder()
                .currentPage(resultPage.getNumber() + 1)
                .totalPage(resultPage.getTotalPages())
                .totalItems(resultPage.getTotalElements())
                .build());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "학습 상세 조회", description = "선택한 학습 내역의 상세 정보를 조회합니다.")
    @GetMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyDetailResponse>> getStudyDetail(
            @Parameter(hidden = true, description = "JWT 인증된 사용자 정보")
            @AuthenticationPrincipal UserDetails userDetails,

            @Parameter(description = "학습 ID", example = "1")
            @PathVariable int studyId
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        StudyDetailResponse detail = studyResultService.getStudyDetail(studyId, userId);
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

}
