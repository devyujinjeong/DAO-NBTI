package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.domain.aggregate.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProblemTestRepository extends JpaRepository<Problem, Integer> {

    // 카테고리 내에서 무작위로 문제 1개씩 (2레벨에서)
    @Query(value = """
    SELECT * FROM problem
    WHERE category_id IN (
        SELECT category_id FROM category WHERE parent_category_id = :parentCategoryId
    )
    AND is_deleted = 'N'
    AND level = 2
    ORDER BY RAND()
    LIMIT 1
    """, nativeQuery = true)
    Optional<Problem> findSampleProblemByParentCategory(@Param("parentCategoryId") int parentCategoryId);

    @Query(value = """
    SELECT * FROM problem
    WHERE category_id IN (
        SELECT category_id FROM category WHERE parent_category_id = :parentCategoryId
    )
    AND is_deleted = 'N'
    AND level = :level
    ORDER BY RAND()
    LIMIT 1
    """, nativeQuery = true)
    Optional<Problem> findFormalProblemByParentCategoryAndLevel(
            @Param("parentCategoryId") int parentCategoryId,
            @Param("level") int level
    );

}
