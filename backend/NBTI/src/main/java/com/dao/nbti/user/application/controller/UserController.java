package com.dao.nbti.user.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.user.application.dto.*;
import com.dao.nbti.user.application.service.UserService;
import com.dao.nbti.user.domain.aggregate.IsDeleted;
import com.dao.nbti.user.exception.UserException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @Operation(summary = "회원가입", description = "사용자는 사용자 정보를 입력하여 회원가입 할 수 있다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 시 데이터 익명화 및 계정 비활성화 처리한다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@AuthenticationPrincipal User user, @RequestBody UserDeleteRequest userDeleteRequest){
        Integer userId = Integer.parseInt(user.getUsername());
        userService.deleteUser(userId,userDeleteRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "아이디 중복 확인", description = "회원가입을 할 경우 해당 아이디가 서비스에 등록되어 있는지 확인한다.")
    @GetMapping("/id-duplicate")
    public ResponseEntity<ApiResponse<IdDuplicateResponse>> idDuplicate(@RequestParam String accountId){
        IdDuplicateResponse response = userService.checkAccountId(accountId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/points")
    @Operation(
            summary = "회원의 포인트 조회", description = "현재 회원이 보유하고 있는 포인트를 조회합니다."
    )
    public ResponseEntity<ApiResponse<Integer>> getPoints(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer userId = Integer.parseInt(userDetails.getUsername());

        int points =  userService.getUserPoint(userId);

        return ResponseEntity.ok(ApiResponse.success(points));
    }

    @Operation(summary = "회원 정보 조회", description = "회원은 자신의 아이디, 이름, 생년월일, 성별, 포인트 등의 정보를 조회 및 필터링할 수 있다.")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal UserDetails userDetails){
        UserInfoResponse response = userService.getUserInfo(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "회원 목록 조회", description = "관리자는 전체 회원 목록을 조회하고 필터/검색 기능을 이용할 수 있다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<UserAdminViewResponse>> getUserList(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String isDeleted,
            @PageableDefault(sort="userId") Pageable pageable){
        IsDeleted deletedEnum = null;
        if (isDeleted != null) {
            deletedEnum = IsDeleted.valueOf(isDeleted);
        }
        UserSearchCondition condition = new UserSearchCondition(accountId,deletedEnum);
        UserAdminViewResponse response = userService.getUserList(condition, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(UserException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}
