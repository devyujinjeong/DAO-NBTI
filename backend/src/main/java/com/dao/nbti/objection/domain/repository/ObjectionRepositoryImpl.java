package com.dao.nbti.objection.domain.repository;

import com.dao.nbti.objection.application.dto.request.AdminObjectionSearchRequest;
import com.dao.nbti.objection.application.dto.response.AdminObjectionDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ObjectionRepositoryImpl implements ObjectionRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AdminObjectionDTO> getObjections(AdminObjectionSearchRequest request) {
        String initQuery = """
        SELECT new com.dao.nbti.objection.application.dto.response.AdminObjectionDTO(
            o.userId,
            CASE WHEN u IS NULL OR u.isDeleted = com.dao.nbti.user.domain.aggregate.IsDeleted.Y THEN '삭제된 유저' ELSE u.accountId END
            , o.objectionId, o.problemId, o.status, o.createdAt
        )
        FROM Objection o
        LEFT JOIN User u ON u.userId = o.userId
        WHERE 1 = 1
        """;
        String jpql = getDynamicQuery(initQuery, request);

        TypedQuery<AdminObjectionDTO> query = em.createQuery(jpql, AdminObjectionDTO.class);

        if (request.getAccountId() != null) {
            query.setParameter("accountId", request.getAccountId());
        }
        if (request.getProblemId() != null) {
            query.setParameter("problemId", request.getProblemId());
        }
        if (request.getStatus() != null) {
            query.setParameter("status", request.getStatus());
        }

        return query
                .setFirstResult(request.getOffset())
                .setMaxResults(request.getLimit())
                .getResultList();
    }

    @Override
    public long countObjectionsBy(AdminObjectionSearchRequest request) {
        String initQuery = """
        SELECT COUNT(o)
        FROM Objection o
        LEFT JOIN User u ON u.userId = o.userId
        WHERE 1 = 1
        """;
        String jpql = getDynamicQuery(initQuery, request);

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        if (request.getAccountId() != null) {
            query.setParameter("accountId", request.getAccountId());
        }
        if (request.getProblemId() != null) {
            query.setParameter("problemId", request.getProblemId());
        }
        if (request.getStatus() != null) {
            query.setParameter("status", request.getStatus());
        }

        return query.getSingleResult();
    }

    private static String getDynamicQuery(String str, AdminObjectionSearchRequest request) {
        StringBuilder jpql = new StringBuilder(str);

        if (request.getAccountId() != null) {
            jpql.append("\nAND u.accountId = :accountId");
        }

        if (request.getProblemId() != null) {
            jpql.append("\nAND o.problemId = :problemId");
        }
        if (request.getStatus() != null) {
            jpql.append("\nAND o.status = :status");
        }
        return jpql.toString();
    }
}
