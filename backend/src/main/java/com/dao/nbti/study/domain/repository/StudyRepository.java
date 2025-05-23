package com.dao.nbti.study.domain.repository;

import com.dao.nbti.study.domain.aggregate.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer> {

    @Query("""
        SELECT DISTINCT s
          FROM Study s
          JOIN StudyResult sr ON sr.studyId = s.studyId
          JOIN Problem      p  ON p.problemId = sr.problemId
          JOIN Category     c  ON c.categoryId = p.categoryId
         LEFT JOIN Category pc ON pc.categoryId = c.parentCategoryId
         WHERE s.userId = :userId
           AND (:year  IS NULL OR FUNCTION('YEAR',  s.createdAt) = :year)
           AND (:month IS NULL OR FUNCTION('MONTH', s.createdAt) = :month)
           AND (:parentCategoryId IS NULL
                OR pc.categoryId = :parentCategoryId)
         ORDER BY s.createdAt DESC
    """)
    Page<Study> findByUserAndCondition(
            @Param("userId")           int      userId,
            @Param("year")             Integer  year,
            @Param("month")            Integer  month,
            @Param("parentCategoryId") Integer  parentCategoryId,
            Pageable pageable
    );

    Optional<Study> findByStudyIdAndUserId(int studyId, int userId);
}
