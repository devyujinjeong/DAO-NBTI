package com.dao.nbti.test.domain.repository;


import com.dao.nbti.test.application.dto.request.AdminTestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.AccountTestResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TestRepositoryCustomImpl implements TestRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AccountTestResultDto> getTest(AdminTestResultSearchCondition condition, Pageable pageable) {
        String initQuery = """
               SELECT new com.dao.nbti.test.application.dto.response.AccountTestResultDto(u.accountId, tr)
               FROM TestResult tr
               JOIN User u
               ON u.userId = tr.userId
               WHERE 1=1
               """;
        String jpql = getDynamicQuery(initQuery, condition);
        TypedQuery<AccountTestResultDto> query = em.createQuery(jpql, AccountTestResultDto.class);

        if (condition.getYear() != null) {
            query.setParameter("year",condition.getYear());
        }

        if (condition.getMonth() != null) {
            query.setParameter("month",condition.getMonth());
        }
        if(condition.getAccountId()!=null){
            query.setParameter("accountId",condition.getAccountId());
        }
        //페이징 처리 적용
        query.setFirstResult((int) pageable.getOffset()); // 시작 위치 (ex: 0, 10, 20...)
        query.setMaxResults(pageable.getPageSize());      // 페이지당 개수

        return query.getResultList();
    }

    @Override
    public long countTest(AdminTestResultSearchCondition condition) {
        String initQuery = """
               SELECT count(*)
               FROM TestResult tr
               JOIN User u
               ON u.userId = tr.userId
               WHERE 1=1
               """;
        String jpql = getDynamicQuery(initQuery, condition);
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        if (condition.getYear() != null) {
            query.setParameter("year",condition.getYear());
        }

        if (condition.getMonth() != null) {
            query.setParameter("month",condition.getMonth());
        }
        if(condition.getAccountId()!=null){
            query.setParameter("accountId",condition.getAccountId());
        }

        return query.getSingleResult();
    }

    private static String getDynamicQuery(String str, AdminTestResultSearchCondition condition) {
        StringBuilder jpql = new StringBuilder(str);

        if (condition.getYear() != null) {
            jpql.append("\nAND FUNCTION('YEAR', tr.createdAt) = :year");
        }

        if (condition.getMonth() != null) {
            jpql.append("\nAND FUNCTION('MONTH', tr.createdAt) = :month");
        }
        if(condition.getAccountId()!=null){
            jpql.append("\nAND u.accountId = :accountId");
        }

        return jpql.toString();
    }
}
