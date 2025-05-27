package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.domain.aggregate.IsDeleted;
import com.dao.nbti.problem.domain.aggregate.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

    // 무작위 3문제 (레벨 무관)
    @Query(value = """
    SELECT * FROM problem
    WHERE category_id IN (
        SELECT category_id FROM category WHERE parent_category_id = :parentCategoryId
    )
    AND is_deleted = 'N'
    ORDER BY RAND()
    LIMIT 3
    """, nativeQuery = true)
    List<Problem> findRandomProblemsByParentCategory(@Param("parentCategoryId") Integer parentCategoryId);

    // 무작위 3문제 (특정 레벨)
    @Query(value = """
    SELECT * FROM problem
    WHERE category_id IN (
        SELECT category_id FROM category WHERE parent_category_id = :parentCategoryId
    )
    AND level = :level
    AND is_deleted = 'N'
    ORDER BY RAND()
    LIMIT 3
    """, nativeQuery = true)
    List<Problem> findRandomProblemsByParentCategoryAndLevel(@Param("parentCategoryId") Integer parentCategoryId,
                                                             @Param("level") int level);

    Optional<Problem> findByProblemIdAndIsDeleted(int problemId, IsDeleted isDeleted);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Problem p SET p.isDeleted = :isDeleted WHERE p.problemId = :problemId
""")
    void deleteByProblemId(int problemId, IsDeleted isDeleted);

    boolean existsByProblemIdAndIsDeleted(int problemId, IsDeleted isDeleted);
}

