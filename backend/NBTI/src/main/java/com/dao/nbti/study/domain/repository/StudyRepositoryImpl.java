package com.dao.nbti.study.domain.repository;

import com.dao.nbti.problem.domain.aggregate.IsDeleted;
import com.dao.nbti.study.application.dto.request.StudySearchRequestDto;
import com.dao.nbti.study.application.dto.response.StudySummaryDto;
import com.dao.nbti.study.domain.aggregate.IsCorrect;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudyRepositoryImpl implements StudyRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<StudySummaryDto> getStudySummaries(StudySearchRequestDto request) {
        String baseQuery = """
            SELECT new com.dao.nbti.study.application.dto.response.StudySummaryDto(
                s.studyId, s.userId, u.accountId, s.createdAt, pc.name, COUNT(sr)
            )
            FROM Study s
            JOIN User u ON s.userId = u.userId
            JOIN StudyResult sr ON sr.studyId = s.studyId
            JOIN Problem p ON p.problemId = sr.problemId
            JOIN Category c ON p.categoryId = c.categoryId
            LEFT JOIN Category pc ON c.parentCategoryId = pc.categoryId
            WHERE p.isDeleted = :isDeleted
        """;

        String jpql = buildQuery(baseQuery, request);

        TypedQuery<StudySummaryDto> query = em.createQuery(jpql + " GROUP BY s.studyId, s.userId, u.accountId ,s.createdAt, pc.name", StudySummaryDto.class);

        query.setParameter("isDeleted", IsDeleted.N);
        setParams(query, request);

        return query.setFirstResult(request.getOffset())
                .setMaxResults(request.getLimit())
                .getResultList();
    }

    @Override
    public long countStudySummaries(StudySearchRequestDto request) {
        String baseQuery = """
            SELECT COUNT(DISTINCT s)
            FROM Study s
            JOIN User u ON s.userId = u.userId
            JOIN StudyResult sr ON sr.studyId = s.studyId
            JOIN Problem p ON p.problemId = sr.problemId
            JOIN Category c ON p.categoryId = c.categoryId
            LEFT JOIN Category pc ON c.parentCategoryId = pc.categoryId
            WHERE p.isDeleted = :isDeleted
        """;

        String jpql = buildQuery(baseQuery, request);

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        query.setParameter("isDeleted", IsDeleted.N);
        setParams(query, request);

        return query.getSingleResult();
    }

    private static String buildQuery(String base, StudySearchRequestDto request) {
        StringBuilder sb = new StringBuilder(base);

        if (request.getAccountId() != null) {
            sb.append("\nAND u.accountId = :accountId");
        }
        if (request.getParentCategoryId() != null) {
            sb.append("\nAND pc.categoryId = :parentCategoryId");
        }
        if (request.getStartDate() != null) {
            sb.append("\nAND s.createdAt >= :startDate");
        }
        if (request.getEndDate() != null) {
            sb.append("\nAND s.createdAt < :endDate");
        }

        return sb.toString();
    }

    private static void setParams(TypedQuery<?> query, StudySearchRequestDto request) {
        if (request.getAccountId() != null) query.setParameter("accountId", request.getAccountId());
        if (request.getParentCategoryId() != null) query.setParameter("parentCategoryId", request.getParentCategoryId());
        if (request.getStartDate() != null) query.setParameter("startDate", request.getStartDate().atStartOfDay());
        if (request.getEndDate() != null) query.setParameter("endDate", request.getEndDate().plusDays(1).atStartOfDay());
    }
}

