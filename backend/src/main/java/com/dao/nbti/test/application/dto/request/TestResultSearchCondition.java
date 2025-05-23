package com.dao.nbti.test.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestResultSearchCondition {
    private Integer year;
    private Integer month;
    private String sort; // "recent" or "past"
    private int userId;
}
