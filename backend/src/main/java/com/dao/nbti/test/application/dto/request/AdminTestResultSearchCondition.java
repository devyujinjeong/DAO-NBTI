package com.dao.nbti.test.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminTestResultSearchCondition {
    private Integer year;
    private Integer month;
    private String accountId;
}
