package com.dao.nbti.test.application.dto.response;

import com.dao.nbti.test.domain.aggregate.TestResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTestResultDto {
    private String accountId;
    private TestResult testResult;
}
