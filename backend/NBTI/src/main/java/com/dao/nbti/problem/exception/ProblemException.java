package com.dao.nbti.problem.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ProblemException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProblemException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
