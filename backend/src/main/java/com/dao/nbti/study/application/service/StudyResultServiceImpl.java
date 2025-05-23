package com.dao.nbti.study.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.study.application.dto.request.StudySearchCondition;
import com.dao.nbti.study.application.dto.response.StudyDetailResponse;
import com.dao.nbti.study.application.dto.response.StudyProblemDetail;
import com.dao.nbti.study.application.dto.response.StudySummaryResponse;
import com.dao.nbti.study.domain.aggregate.IsCorrect;
import com.dao.nbti.study.domain.aggregate.Study;
import com.dao.nbti.study.domain.aggregate.StudyResult;
import com.dao.nbti.study.domain.repository.StudyRepository;
import com.dao.nbti.study.domain.repository.StudyResultRepository;
import com.dao.nbti.study.exception.StudyException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyResultServiceImpl implements StudyResultService {

    private final StudyRepository studyRepository;
    private final StudyResultRepository studyResultRepository;
    private final ProblemRepository problemRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public Page<StudySummaryResponse> getStudyList(StudySearchCondition condition, Pageable pageable) {
        Page<Study> results = studyRepository.findByUserAndCondition(
                condition.getUserId(),
                condition.getYear(),
                condition.getMonth(),
                condition.getParentCategoryId(),
                pageable
        );

        return results.map(study -> {
            int studyId = study.getStudyId();

            int totalCount = studyResultRepository.countByStudyId(studyId);
            int correctCount = studyResultRepository.countByStudyIdAndIsCorrect(studyId, IsCorrect.Y);

            List<String> parentCategories = studyResultRepository.findAllParentCategoryNamesByStudyId(studyId);
            if (parentCategories.isEmpty()) {
                throw new StudyException(ErrorCode.PARENT_CATEGORY_NOT_FOUND);
            }

            String parentCategoryName = parentCategories.get(0);

            return StudySummaryResponse.builder()
                    .studyId(studyId)
                    .parentCategoryName(parentCategoryName)
                    .createdAt(study.getCreatedAt())
                    .correctCount(correctCount)
                    .totalCount(totalCount)
                    .build();
        });
    }

    @Override
    public StudyDetailResponse getStudyDetail(int studyId, int userId) {
        // 1. 학습 주인 검증
        Study study = studyRepository.findByStudyIdAndUserId(studyId, userId)
                .orElseThrow(() -> new StudyException(ErrorCode.UNAUTHORIZED_TEST_RESULT_ACCESS)); // 이름 재활용

        // 2. 학습 결과 목록 조회
        List<StudyResult> studyResults = studyResultRepository.findByStudyId(studyId);

        if (studyResults.isEmpty()) {
            throw new StudyException(ErrorCode.PROBLEM_NOT_FOUND);
        }

        // 3. 문제 ID로 문제들 조회
        List<Integer> problemIds = studyResults.stream()
                .map(StudyResult::getProblemId)
                .toList();

        Map<Integer, Problem> problemMap = problemRepository.findAllById(problemIds).stream()
                .collect(Collectors.toMap(Problem::getProblemId, p -> p));

        // 4. 첫 번째 문제로부터 상위 카테고리 정보 가져오기
        Problem firstProblem = problemMap.get(problemIds.get(0));
        Category subCategory = categoryRepository.findById(firstProblem.getCategoryId())
                .orElseThrow(() -> new StudyException(ErrorCode.CATEGORY_NOT_FOUND));

        Category parentCategory = categoryRepository.findById(subCategory.getParentCategoryId())
                .orElseThrow(() -> new StudyException(ErrorCode.PARENT_CATEGORY_NOT_FOUND));

        // 5. 문제 상세 DTO 구성
        List<StudyProblemDetail> problems = studyResults.stream().map(sr -> {
            Problem p = problemMap.get(sr.getProblemId());
            return StudyProblemDetail.builder()
                    .problemId(p.getProblemId())
                    .imageUrl(p.getContentImageUrl())
                    .correctAnswer(p.getCorrectAnswer())
                    .userAnswer(sr.getAnswer())
                    .isCorrect(sr.getIsCorrect() == IsCorrect.Y)
                    .level(p.getLevel())
                    .build();
        }).toList();

        return StudyDetailResponse.builder()
                .parentCategoryName(parentCategory.getName())
                .parentCategoryDescription(parentCategory.getDescription())
                .createdAt(study.getCreatedAt())
                .problems(problems)
                .build();
    }

}





