package com.dao.nbti.test.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class AdminTestResultSummaryResponse {
    Page<AdminTestResultSummaryDTO> page;

}
