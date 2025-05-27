package com.dao.nbti.user.application.dto;

import com.dao.nbti.user.domain.aggregate.IsDeleted;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSearchCondition {
    private String accountId;
    private IsDeleted isDeleted;
}
