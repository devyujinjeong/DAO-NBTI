package com.dao.nbti.user.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
