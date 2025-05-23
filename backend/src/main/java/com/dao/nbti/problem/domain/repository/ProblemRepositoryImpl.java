package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO;
import com.dao.nbti.problem.domain.aggregate.IsDeleted;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProblemRepositoryImpl implements ProblemRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProblemSummaryDTO> getProblemsBy(ProblemSearchRequest request) {
        String initQuery = """
        SELECT new com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO(
            p.problemId, p.categoryId, p.level, pc.name, p.answerTypeId, c.name
        )
        FROM Problem p
        JOIN Category c ON p.categoryId = c.categoryId
        LEFT JOIN Category pc ON c.parentCategoryId = pc.categoryId
        WHERE p.isDeleted = :isDeleted
        """;
        String jpql = getDynamicQuery(initQuery, request, true);

        TypedQuery<ProblemSummaryDTO> query = em.createQuery(jpql, ProblemSummaryDTO.class);

        query.setParameter("isDeleted", IsDeleted.N);
        if (request.getParentCategoryId() != null) {
            query.setParameter("parentCategoryId", request.getParentCategoryId());
        }
        if (request.getChildCategoryId() != null) {
            query.setParameter("categoryId", request.getChildCategoryId());
        }
        if (request.getLevel() != null) {
            query.setParameter("level", request.getLevel());
        }


        return query
                .setFirstResult(request.getOffset())
                .setMaxResults(request.getLimit())
                .getResultList();
    }

    @Override
    public long countProblemsBy(ProblemSearchRequest request) {
        String initQuery = """
        SELECT COUNT(p)
        FROM Problem p
        JOIN Category c ON p.categoryId = c.categoryId
        LEFT JOIN Category pc ON c.parentCategoryId = pc.categoryId
        WHERE p.isDeleted = :isDeleted
        """;
        String jpql = getDynamicQuery(initQuery, request, false);

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("isDeleted", IsDeleted.N);
        if (request.getParentCategoryId() != null) {
            query.setParameter("parentCategoryId", request.getParentCategoryId());
        }
        if (request.getChildCategoryId() != null) {
            query.setParameter("categoryId", request.getChildCategoryId());
        }
        if (request.getLevel() != null) {
            query.setParameter("level", request.getLevel());
        }

        return query.getSingleResult();
    }

    private static String getDynamicQuery(String str, ProblemSearchRequest request, boolean isOrdered) {
        StringBuilder jpql = new StringBuilder(str);

        if (request.getParentCategoryId() != null) {
            jpql.append("\nAND pc.categoryId = :parentCategoryId");
        }

        if (request.getChildCategoryId() != null) {
            jpql.append("\nAND p.categoryId = :categoryId");
        }
        if (request.getLevel() != null) {
            jpql.append("\nAND p.level = :level");
        }

        if (isOrdered) {
            jpql.append("\nORDER BY p.problemId ASC");
        }
        return jpql.toString();
    }
}
