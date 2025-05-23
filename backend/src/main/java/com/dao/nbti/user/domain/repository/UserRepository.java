package com.dao.nbti.user.domain.repository;


import com.dao.nbti.user.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByAccountId(String accountId);

    Optional<User> findByAccountIdAndDeletedAtIsNull(String accountId);

    Optional<User> findByUserIdAndDeletedAtIsNull(Integer userId);

    Optional<User> findByAccountIdAndDeletedAtIsNullAndName(String accountId, String name);

    Optional<User> findByUserId(Integer userId);

}
