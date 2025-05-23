package com.dao.nbti.user.domain.repository;

import com.dao.nbti.user.application.dto.UserSearchCondition;
import com.dao.nbti.user.domain.aggregate.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> getUsers(UserSearchCondition condition, Pageable pageable);

    long countUsers(UserSearchCondition condition);
}
