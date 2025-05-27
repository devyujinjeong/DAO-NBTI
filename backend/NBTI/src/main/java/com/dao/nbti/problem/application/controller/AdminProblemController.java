package com.dao.nbti.problem.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.application.dto.request.ProblemCreateRequest;
import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.request.ProblemUpdateRequest;
import com.dao.nbti.problem.application.dto.response.ProblemDeleteResponse;
import com.dao.nbti.problem.application.dto.response.ProblemDetailsResponse;
import com.dao.nbti.problem.application.dto.response.ProblemListResponse;
import com.dao.nbti.problem.application.service.AdminProblemService;
import com.dao.nbti.problem.exception.ProblemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/problems")
@RequiredArgsConstructor
@Tag(name = "문제 관리", description = "관리자 문제 관리 기능 API")
public class AdminProblemController {

    private final AdminProblemService adminProblemService;

    @GetMapping("/list")
    @Operation(summary = "문제 목록 조회", description = "관리자가 서비스에 등록된 전체 또는 필터링된 문제 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ProblemListResponse>> getProblems(@ModelAttribute ProblemSearchRequest problemSearchRequest) {

        return ResponseEntity.ok(ApiResponse.success(adminProblemService.getProblems(problemSearchRequest)));
    }

    @GetMapping("/{problemId}")
    @Operation(summary = "문제 상세 조회", description = "관리자가 서비스에 등록된 문제 세부 내용을 조회합니다.")
    public ResponseEntity<ApiResponse<ProblemDetailsResponse>> getProblemDetails(
            @Parameter(description = "문제 ID", example = "10")
            @PathVariable int problemId
    ) {

        return ResponseEntity.ok(ApiResponse.success(adminProblemService.getProblemDetails(problemId)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "문제 등록", description = "관리자가 서비스에 문제를 등록합니다. 이미지 파일은 필수입니다.")
    public ResponseEntity<ApiResponse<ProblemDetailsResponse>> createProblem(
            @RequestPart @Validated ProblemCreateRequest problemCreateRequest,
            @Parameter(
                    description = "multipart/form-data 형식의 이미지 파일. key는 imageFile 입니다.",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return ResponseEntity.ok(ApiResponse.success(adminProblemService.createProblem(problemCreateRequest, imageFile)));
    }

    @PutMapping(value = "/{problemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "문제 수정", description = "관리자가 서비스에 등록된 문제를 수정합니다. 이미지를 새로 첨부하면 교체되고, 아니면 기존 이미지 유지됩니다.")
    public ResponseEntity<ApiResponse<ProblemDetailsResponse>> updateProblem(
            @RequestPart @Validated ProblemUpdateRequest problemUpdateRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @Parameter(description = "문제 ID", example = "10")
            @PathVariable int problemId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                adminProblemService.updateProblem(problemUpdateRequest, problemId, imageFile)
        ));
    }


    @DeleteMapping("/{problemId}")
    @Operation(summary = "문제 삭제", description = "관리자가 서비스에 등록된 문제를 삭제합니다.")
    public ResponseEntity<ApiResponse<ProblemDeleteResponse>> deleteProblem(
            @Parameter(description = "문제 ID", example = "10")
            @PathVariable int problemId
    ) {

        return ResponseEntity.ok(ApiResponse.success(adminProblemService.deleteProblem(problemId)));
    }

    @ExceptionHandler(ProblemException.class)
    public ResponseEntity<ApiResponse<Void>> handleProblemException(ProblemException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}
