package com.dao.nbti.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    //회원 오류 (00000 ~ 09999)
    LOGIN_ID_ALREADY_EXISTS("10001", "이미 사용중인 ID 입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("10002","존재하지 않는 사용자입니다.",HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("10003", "올바르지 않은 아이디 혹은 비밀번호입니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_DISCORD("10004","비밀번호를 다시 입력해주세요",HttpStatus.BAD_REQUEST),

    // 학습 관련 오류 (20001 ~ 29999)
    CATEGORY_NOT_FOUND("20001", "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),
    ANSWER_TYPE_NOT_FOUND("20002", "존재하지 않는 답안 유형입니다.", HttpStatus.NOT_FOUND),
    PROBLEM_NOT_FOUND("20003", "해당하는 문제가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PARENT_CATEGORY_NOT_FOUND("20004", "상위 분야를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PROBLEM_DELETED("20005", "삭제된 문제입니다.", HttpStatus.GONE),
    STUDY_RESULT_NOT_FOUND("20006", "학습 결과가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    STUDY_NOT_FOUND("20007", "학습이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    STUDY_ACCESS_DENIED("20008", "학습을 조회할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 검사 결과 오류 (30000 ~ 39999)
    TEST_RESULT_NOT_FOUND("30001", "검사 결과를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_TEST_RESULT_ACCESS("30002", "본인의 검사 결과만 조회할 수 있습니다.", HttpStatus.FORBIDDEN),
    AI_CALL_FAILED("30003", "ai 호출에 실패했습니다.", HttpStatus.BAD_GATEWAY),
    NOT_ENOUGH_POINT("30004", "포인트가 부족합니다.", HttpStatus.FORBIDDEN),
    NOT_YOUR_RESULT("30005", "이 검사 결과는 본인의 것이 아닙니다.", HttpStatus.NOT_ACCEPTABLE),

    // 이의 제기 오류 (40000 ~ 49999)
    OBJECTION_NOT_FOUND("40001", "이의 제기 항목을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_OBJECTION_ACCESS("40002", "본인의 이의 제기만 조회할 수 있습니다.", HttpStatus.FORBIDDEN),
    DUPLICATE_OBJECTION("40003", "이미 해당 문제에 대해 이의 제기를 제출했습니다.", HttpStatus.CONFLICT),
    INVALID_OBJECTION_TARGET("40004", "해당 문제는 사용자가 학습하지 않은 항목입니다.", HttpStatus.BAD_REQUEST),
    OBJECTION_ALREADY_UPDATED("40005", "이미 처리 완료된 이의 제기입니다.", HttpStatus.BAD_REQUEST),
    INVALID_STATUS("40006", "변경할 상태는 승인 또는 반려여야 합니다.", HttpStatus.BAD_REQUEST),
    REJECTION_REASON_REQUIRED("40007", "반려 사유를 입력해야 합니다.", HttpStatus.BAD_REQUEST),

    // 공통 오류
    VALIDATION_ERROR("90001", "입력 값 검증 오류입니다.", HttpStatus.BAD_REQUEST),
    UNKNOWN_RUNTIME_ERROR("90002", "알 수 없는 런타임 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR("90003", "알 수 없는 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
