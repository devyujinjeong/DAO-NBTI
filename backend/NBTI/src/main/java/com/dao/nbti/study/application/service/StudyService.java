package com.dao.nbti.study.application.service;

import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.domain.aggregate.AnswerType;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.AnswerTypeRepository;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.study.application.dto.request.StudySearchRequestDto;
import com.dao.nbti.study.application.dto.request.SubmitStudyRequestDto;
import com.dao.nbti.study.application.dto.response.*;
import com.dao.nbti.study.domain.aggregate.IsCorrect;
import com.dao.nbti.study.domain.aggregate.Study;
import com.dao.nbti.study.domain.aggregate.StudyResult;
import com.dao.nbti.study.domain.repository.StudyRepository;
import com.dao.nbti.study.domain.repository.StudyRepositoryCustom;
import com.dao.nbti.study.domain.repository.StudyResultRepository;
import com.dao.nbti.study.exception.*;
import com.dao.nbti.user.domain.aggregate.Authority;
import com.dao.nbti.user.domain.aggregate.PointHistory;
import com.dao.nbti.user.domain.aggregate.PointType;
import com.dao.nbti.user.domain.repository.PointHistoryRepository;
import com.dao.nbti.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyRepositoryCustom studyRepositoryCustom;
    private final StudyResultRepository studyResultRepository;
    private final ProblemRepository problemRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerTypeRepository answerTypeRepository;
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(readOnly = true)
    public StudyCategoryListResponseDto getStudyCategoryList() {
        List<Category> categoryList = categoryRepository.findByParentCategoryIsNull();

        return StudyCategoryListResponseDto.from(categoryList);
    }

    @Transactional(readOnly = true)
    public List<ProblemResponseDto> getProblems(Integer parentCategoryId, int level) {
        List<Problem> problems = (level == 0)
                ? problemRepository.findRandomProblemsByParentCategory(parentCategoryId)
                : problemRepository.findRandomProblemsByParentCategoryAndLevel(parentCategoryId, level);

        // 문제가 없을 경우 예외 처리
        if (problems.isEmpty()) {
            throw new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND);
        }

        return problems.stream().map(p -> {
            Category category = categoryRepository.findById(p.getCategoryId())
                    .orElseThrow(() -> new NoSuchCategoryException(ErrorCode.CATEGORY_NOT_FOUND));
            AnswerType answerType = answerTypeRepository.findById(p.getAnswerTypeId())
                    .orElseThrow(() -> new NoSuchAnswerTypeException(ErrorCode.ANSWER_TYPE_NOT_FOUND));

            return ProblemResponseDto.builder()
                    .problemId(p.getProblemId())
                    .categoryId(p.getCategoryId())
                    .categoryName(category.getName())
                    .answerTypeId(p.getAnswerTypeId())
                    .correctAnswer(p.getCorrectAnswer())
                    .answerTypeDescription(answerType.getDescription())
                    .contentImageUrl(p.getContentImageUrl())
                    .level(p.getLevel())
                    .timeLimit(category.getTimeLimit())
                    .build();
        }).toList();
    }

    @Transactional
    public SubmitStudyResponseDto gradeAndSaveResults(int userId, SubmitStudyRequestDto request) {
        Study study = studyRepository.save(
                Study.builder()
                        .userId(userId)
                        .build()
        );

        // 학습 결과 엔티티 저장
        for (SubmitStudyRequestDto.AnswerSubmission submission : request.getAnswers()) {
            Problem problem = problemRepository.findById(submission.getProblemId())
                    .orElseThrow(() -> new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));

            boolean isCorrect = problem.getCorrectAnswer().equals(submission.getUserAnswer());

            studyResultRepository.save(StudyResult.builder()
                    .studyId(study.getStudyId())
                    .problemId(problem.getProblemId())
                    .isCorrect(isCorrect ? IsCorrect.Y : IsCorrect.N)
                    .answer(submission.getUserAnswer())
                    .build());
        }

        // userId에 해당하는 유저에게 포인트 1 적립, 포인트 적립 기록
        userRepository.findById(userId).ifPresent(user -> {
            user.addPoint(); // +1
            pointHistoryRepository.save(PointHistory.builder()
                    .userId(user.getUserId())
                    .point(1)
                    .pointType(PointType.SAVE)
                    .createdAt(LocalDateTime.now())
                    .build());
        });

        return SubmitStudyResponseDto.builder()
                .studyId(study.getStudyId())
                .build();
    }

    @Transactional(readOnly = true)
    public StudyResultDetailResponseDto getStudyResultDetail(int studyId, UserDetails userDetails) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException(ErrorCode.STUDY_NOT_FOUND));

        // 학습의 주인이 요청한 사람인지 확인
        boolean isOwner = study.getUserId().equals(Integer.parseInt(userDetails.getUsername()));

        // 혹은 요청한 사람이 ADMIN인지 확인
        String authorityName = userDetails.getAuthorities().iterator().next().getAuthority();
        Authority role = Authority.valueOf(authorityName); // 문자열 → enum 변환

        boolean isAdmin = role == Authority.ADMIN;

        // ADMIN도 아니고, 학습한 사람도 아니면 에러처리
        if (!isOwner && !isAdmin) {
            throw new InvalidAccessException(ErrorCode.STUDY_ACCESS_DENIED);
        }

        List<StudyResult> results = studyResultRepository.findByStudyId(studyId);
        if (results.isEmpty()) {
            throw new StudyResultNotFoundException(ErrorCode.STUDY_RESULT_NOT_FOUND);
        }

        List<StudyResultDetailResponseDto.StudyResultItem> resultItems = results.stream().map(result -> {
            Problem problem = problemRepository.findById(result.getProblemId())
                    .orElseThrow(() -> new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));

            Category category = categoryRepository.findById(problem.getCategoryId())
                    .orElseThrow(() -> new NoSuchCategoryException(ErrorCode.CATEGORY_NOT_FOUND));

            Category parentCategory = categoryRepository.findById(category.getParentCategoryId())
                    .orElseThrow(() -> new NoSuchCategoryException(ErrorCode.CATEGORY_NOT_FOUND));

            return StudyResultDetailResponseDto.StudyResultItem.builder()
                    .studyResultId(result.getStudyResultId())
                    .problemId(problem.getProblemId())
                    .isCorrect(result.getIsCorrect() == IsCorrect.Y)
                    .level(problem.getLevel())
                    .correctAnswer(problem.getCorrectAnswer())
                    .submittedAnswer(result.getAnswer())
                    .parentCategoryName(parentCategory.getName())
                    .contentImageUrl(problem.getContentImageUrl())
                    .build();
        }).toList();

        int correctCount = (int) results.stream()
                .filter(r -> r.getIsCorrect() == IsCorrect.Y)
                .count();

        return StudyResultDetailResponseDto.builder()
                .studyId(studyId)
                .totalCount(resultItems.size())
                .correctCount(correctCount)
                .results(resultItems)
                .build();
    }

    @Transactional(readOnly = true)
    public StudySummaryListResponseDto getStudySummaries(StudySearchRequestDto request) {
        List<StudySummaryDto> list = studyRepositoryCustom.getStudySummaries(request);
        list.forEach(dto -> System.out.println(dto.getStudyId()));
        long total = studyRepositoryCustom.countStudySummaries(request);
        System.out.println(total);

        return StudySummaryListResponseDto.builder()
                .studies(list)
                .pagination(Pagination.builder()
                        .currentPage(request.getPage())
                        .totalItems(total)
                        .totalPage((int) Math.ceil((double) total / request.getSize()))
                        .build())
                .build();
    }


}
