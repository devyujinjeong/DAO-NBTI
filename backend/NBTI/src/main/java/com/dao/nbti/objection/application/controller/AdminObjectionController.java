package com.dao.nbti.objection.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.objection.application.dto.request.AdminObjectionSearchRequest;
import com.dao.nbti.objection.application.dto.request.ObjectionUpdateRequest;
import com.dao.nbti.objection.application.dto.response.AdminObjectionDetailResponse;
import com.dao.nbti.objection.application.dto.response.AdminObjectionListResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionUpdateResponse;
import com.dao.nbti.objection.application.service.AdminObjectionService;
import com.dao.nbti.objection.exception.ObjectionException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/objections")
@RequiredArgsConstructor
@Tag(name = "이의 제기 관리", description = "관리자 이의 제기 관리 API")
public class AdminObjectionController {

    private final AdminObjectionService adminObjectionService;

    @Operation(summary = "이의 제기 목록 조회", description = "관리자가 이의 제기 목록을 조회합니다. 회원 아이디, 문제 ID, 상태로 필터링할 수 있습니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<AdminObjectionListResponse>> getObjections(
            @ModelAttribute AdminObjectionSearchRequest adminObjectionSearchRequest
            ) {
        AdminObjectionListResponse objections = adminObjectionService.getObjections(adminObjectionSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(objections));
    }

    @Operation(summary = "이의 제기 상세 조회", description = "관리자가 이의 제기 상세 내용을 조회합니다.")
    @GetMapping("/{objectionId}")
    public ResponseEntity<ApiResponse<AdminObjectionDetailResponse>> getObjectionDetails(
            @Parameter(description="이의 제기 ID", example="11")
            @PathVariable int objectionId
    ) {
        AdminObjectionDetailResponse objection = adminObjectionService.getObjectionDetails(objectionId);
        return ResponseEntity.ok(ApiResponse.success(objection));
    }

    @Operation(summary = "이의 제기 처리", description = "관리자가 이의 제기를 승인 또는 반려합니다. 반려 시 사유를 반드시 입력해야 합니다.")
    @PutMapping("/{objectionId}")
    public ResponseEntity<ApiResponse<ObjectionUpdateResponse>> updateObjection(
            @Parameter(description="이의 제기 ID", example="11")
            @PathVariable int objectionId, @Valid @RequestBody ObjectionUpdateRequest objectionUpdateRequest
    ) {
        ObjectionUpdateResponse objection = adminObjectionService.updateObjection(objectionId, objectionUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(objection));
    }

    @ExceptionHandler(ObjectionException.class)
    public ResponseEntity<ApiResponse<Void>> handleObjectionException(ObjectionException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
