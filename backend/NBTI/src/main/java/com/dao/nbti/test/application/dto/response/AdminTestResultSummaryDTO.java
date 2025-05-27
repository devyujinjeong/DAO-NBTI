package com.dao.nbti.test.application.dto.response;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdminTestResultSummaryDTO {
    private int testResultId;
    private String accountId;
    private LocalDateTime createdAt;
    private String highestCategory;
    private String lowestCategory;
    private int totalScore;

    public void setAccountId(String accountId){
        this.accountId = accountId;
    }
}
