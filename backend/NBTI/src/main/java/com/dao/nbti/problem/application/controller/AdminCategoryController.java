package com.dao.nbti.problem.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.problem.application.dto.response.CategoryResponse;
import com.dao.nbti.problem.application.service.AdminProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Tag(name = "문제 분야 조회", description = "관리자 문제 분야 조회 기능 API")
public class AdminCategoryController {

    private final AdminProblemService adminProblemService;

    @GetMapping
    @Operation(summary = "분야 조회", description = "관리자가 서비스에 등록된 모든 상위 분야 및 하위 분야를 조회합니다.")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategories() {

        return ResponseEntity.ok(ApiResponse.success(adminProblemService.getCategories()));
    }

}
