package com.dao.nbti.user.domain.repository;


import com.dao.nbti.user.application.dto.UserSearchCondition;
import com.dao.nbti.user.domain.aggregate.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> getUsers(UserSearchCondition condition, Pageable pageable) {
        String initQuery = """
               SELECT u FROM User u WHERE 1=1
               """;
        String jpql = getDynamicQuery(initQuery, condition);
        TypedQuery<User> query = em.createQuery(jpql, User.class);

        if (condition.getAccountId() != null) {
            log.info("{}",condition.getAccountId());
            query.setParameter("accountId",condition.getAccountId());
        }
        if(condition.getIsDeleted()!=null){
            query.setParameter("isDeleted",condition.getIsDeleted());
        }

        //페이징 처리 적용
        query.setFirstResult((int) pageable.getOffset()); // 시작 위치 (ex: 0, 10, 20...)
        query.setMaxResults(pageable.getPageSize());      // 페이지당 개수

        return query.getResultList();
    }

    @Override
    public long countUsers(UserSearchCondition condition) {
        String initQuery = """
               SELECT count(u) FROM User u WHERE 1=1
                """;
        String jpql = getDynamicQuery(initQuery, condition);
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        if (condition.getAccountId() != null) {
            query.setParameter("accountId",condition.getAccountId());
        }
        if(condition.getIsDeleted()!=null){
            query.setParameter("isDeleted",condition.getIsDeleted());
        }

        return query.getSingleResult();
    }

    private static String getDynamicQuery(String str, UserSearchCondition condition) {
        StringBuilder jpql = new StringBuilder(str);

        if (condition.getAccountId() != null) {
            jpql.append("\nAND u.accountId = :accountId");
        }

        if (condition.getIsDeleted() != null) {
            jpql.append("\nAND u.isDeleted = :isDeleted");
        }

        return jpql.toString();
    }
}
