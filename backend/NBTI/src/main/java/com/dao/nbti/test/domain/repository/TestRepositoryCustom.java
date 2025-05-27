package com.dao.nbti.test.domain.repository;

import com.dao.nbti.test.application.dto.request.AdminTestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.AccountTestResultDto;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TestRepositoryCustom {
    List<AccountTestResultDto> getTest(AdminTestResultSearchCondition condition, Pageable pageable);

    long countTest(AdminTestResultSearchCondition condition);
}
