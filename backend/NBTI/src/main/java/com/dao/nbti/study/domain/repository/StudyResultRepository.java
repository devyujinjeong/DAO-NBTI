package com.dao.nbti.study.domain.repository;

import com.dao.nbti.study.domain.aggregate.IsCorrect;
import com.dao.nbti.study.domain.aggregate.StudyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyResultRepository extends JpaRepository<StudyResult, Integer> {
    
    int countByStudyId(int studyId);

    int countByStudyIdAndIsCorrect(int studyId, IsCorrect isCorrect);

    @Query("""
        SELECT pc.name FROM StudyResult sr
        JOIN Problem p ON sr.problemId = p.problemId
        JOIN Category c ON p.categoryId = c.categoryId
        JOIN Category pc ON c.parentCategoryId = pc.categoryId
        WHERE sr.studyId = :studyId
        GROUP BY pc.name
    """)
    List<String> findAllParentCategoryNamesByStudyId(@Param("studyId") int studyId);

    List<StudyResult> findByStudyId(int studyId);

    @Query("""
        SELECT CASE WHEN COUNT(sr) > 0 THEN true ELSE false END
        FROM StudyResult sr
        JOIN Study s ON sr.studyId = s.studyId
        WHERE s.userId = :userId AND sr.problemId = :problemId
    """)
    boolean existsByUserIdAndProblemId(@Param("userId") int userId, @Param("problemId") int problemId);
    
}
