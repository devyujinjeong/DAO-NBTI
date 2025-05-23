package com.dao.nbti.objection.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ObjectionException extends RuntimeException {
    private final ErrorCode errorCode;

    public ObjectionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
