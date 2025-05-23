package com.dao.nbti.study.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.study.application.dto.request.SubmitStudyRequestDto;
import com.dao.nbti.study.application.dto.response.ProblemResponseDto;
import com.dao.nbti.study.application.dto.response.StudyCategoryListResponseDto;
import com.dao.nbti.study.application.dto.response.StudyResultDetailResponseDto;
import com.dao.nbti.study.application.dto.response.SubmitStudyResponseDto;
import com.dao.nbti.study.application.service.StudyService;
import com.dao.nbti.study.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
@Tag(name = "학습", description = "학습 문제, 기록, 결과 조회")
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/category")
    @Operation(summary = "학습 분야 목록 조회", description = "학습 시 선택 가능한 상위 분야 6개의 목록 및 그에 대한 설명을 제공합니다.")
    public ResponseEntity<ApiResponse<StudyCategoryListResponseDto>> getStudyCategoryList() {
        StudyCategoryListResponseDto studyCategoryList = studyService.getStudyCategoryList();
        return ResponseEntity.ok(ApiResponse.success(studyCategoryList));
    }

    @GetMapping("/problem")
    @Operation(summary = "학습 문제 3개 제공", description = "선택한 분야와 난이도에 따라 3문제를 제공합니다. level=0이면 무작위 난이도입니다.")
    public ResponseEntity<ApiResponse<List<ProblemResponseDto>>> getStudyProblems(
            @RequestParam Integer categoryId,
            @RequestParam int level
    ) {
        List<ProblemResponseDto> problems = studyService.getProblems(categoryId, level);
        return ResponseEntity.ok(ApiResponse.success(problems));
    }

    @PostMapping("/submit")
    @Operation(summary = "학습 제출", description = "제출된 문제 답안을 채점하고 학습을 기록하며 1포인트를 지급합니다.")
    public ResponseEntity<ApiResponse<SubmitStudyResponseDto>> submitAnswers(
            @RequestBody @Valid SubmitStudyRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        SubmitStudyResponseDto response = studyService.gradeAndSaveResults(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @GetMapping("/result/{studyId}")
    @Operation(summary = "학습 결과 조회", description = "해당 학습 ID의 문제 채점 결과를 반환합니다.")
    public ResponseEntity<ApiResponse<StudyResultDetailResponseDto>> getStudyResult(
            @PathVariable int studyId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        StudyResultDetailResponseDto response = studyService.getStudyResultDetail(studyId, userDetails);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @ExceptionHandler(NoSuchCategoryException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchCategoryException(NoSuchCategoryException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(NoSuchAnswerTypeException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchAnswerTypeException(NoSuchAnswerTypeException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(ProblemNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProblemNotFoundException(ProblemNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(InvalidAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidAccessException(InvalidAccessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(StudyNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStudyNotFoundException(StudyNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(StudyResultNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStudyResultNotFoundException(StudyResultNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
