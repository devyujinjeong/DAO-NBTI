package com.dao.nbti.study.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ProblemNotFoundException extends RuntimeException {
   private final ErrorCode errorCode;

   public ProblemNotFoundException(final ErrorCode errorCode) {
       super(errorCode.getMessage());
       this.errorCode = errorCode;
   }
}