package com.dao.nbti.test.domain.repository;

import com.dao.nbti.test.domain.aggregate.TestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {

    /**
     * 사용자 ID와 검사 결과 ID로 상세 조회 (소유권 확인용)
     */
    Optional<TestResult> findByTestResultIdAndUserId(int testResultId, int userId);

    /**
     * 사용자별 검사 결과 목록 조회 (조건: 년, 월)
     * 정렬은 Pageable의 Sort를 따름
     */
    @Query("""
        SELECT t FROM TestResult t
        WHERE t.userId = :userId
        AND t.isSaved = com.dao.nbti.test.domain.aggregate.IsSaved.Y
        AND (:year IS NULL OR FUNCTION('YEAR', t.createdAt) = :year)
        AND (:month IS NULL OR FUNCTION('MONTH', t.createdAt) = :month)
        ORDER BY t.createdAt DESC
    """)
    Page<TestResult> findByUserAndCondition(
            @Param("userId") int userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            Pageable pageable
    );

    @Query(value = """
        SELECT *
        FROM test_result
        WHERE user_id = :userId
        ORDER BY created_at DESC
        LIMIT 1
    """, nativeQuery = true)
    Optional<TestResult> findLatestByUserId(@Param("userId") int userId);

    Optional<TestResult> findByTestResultId(int testResultId);
}