package com.dao.nbti.study.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchAnswerTypeException extends RuntimeException {
    private final ErrorCode errorCode;

    public NoSuchAnswerTypeException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
