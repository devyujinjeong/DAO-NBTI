package com.dao.nbti.study.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class StudyResultNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public StudyResultNotFoundException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
