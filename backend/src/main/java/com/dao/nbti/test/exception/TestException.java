package com.dao.nbti.test.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class TestException extends RuntimeException {
    private final ErrorCode errorCode;

    public TestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
