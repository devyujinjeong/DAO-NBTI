package com.dao.nbti.objection.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.objection.application.dto.request.ObjectionCreateRequest;
import com.dao.nbti.objection.application.dto.response.ObjectionCreateResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionDetailResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.application.service.ObjectionService;
import com.dao.nbti.objection.domain.aggregate.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mypage/objections")
@RequiredArgsConstructor
@Tag(name = "이의 제기", description = "사용자 이의 제기 조회 API")
public class ObjectionController {

    private final ObjectionService objectionService;

    @Operation(summary = "이의 제기 목록 조회", description = "로그인한 사용자의 이의 제기 내역을 상태별로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getObjections(
            @Parameter(description = "이의 제기 상태 필터", example = "PENDING")
            @RequestParam(required = false) Status status,

            @Parameter(hidden = true, description = "JWT 인증된 사용자 정보")
            @AuthenticationPrincipal UserDetails userDetails,

            @Parameter(description = "상위 분야 ID", example = "1")
            @RequestParam(required = false) Integer parentCategoryId,

            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        Page<ObjectionSummaryResponse> page = objectionService.getObjectionsByUser(userId, status, parentCategoryId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("pagination", Pagination.builder()
                .currentPage(page.getNumber() + 1)
                .totalPage(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .build());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "이의 제기 상세 조회", description = "선택한 이의 제기 항목의 상세 정보를 조회합니다.")
    @GetMapping("/{objectionId}")
    public ResponseEntity<ApiResponse<ObjectionDetailResponse>> getObjectionDetail(
            @Parameter(description = "조회할 이의 제기 ID", example = "7")
            @PathVariable int objectionId,

            @Parameter(hidden = true, description = "JWT 인증된 사용자 정보")
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        ObjectionDetailResponse detail = objectionService.getObjectionDetail(objectionId, userId);
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    @PostMapping
    @Operation(summary = "이의 제기 등록", description = "문제에 대한 이의 제기를 등록합니다. 사유는 필수이며, 동일 문제에 대해 중복 등록할 수 없습니다.")
    public ResponseEntity<ApiResponse<ObjectionCreateResponse>>  createObjectionTest(
            @RequestBody @Valid ObjectionCreateRequest request,

            @Parameter(hidden = true, description = "JWT 인증된 사용자 정보")
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int userId = Integer.parseInt(userDetails.getUsername());
        ObjectionCreateResponse response = objectionService.createObjection(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
