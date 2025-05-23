package com.dao.nbti.study.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchCategoryException extends RuntimeException {
   private final ErrorCode errorCode;

   public NoSuchCategoryException(final ErrorCode errorCode) {
       super(errorCode.getMessage());
       this.errorCode = errorCode;
   }
}