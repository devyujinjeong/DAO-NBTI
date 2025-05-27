package com.dao.nbti.problem.application.service;

import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.application.dto.request.ProblemCommandRequest;
import com.dao.nbti.problem.application.dto.request.ProblemCreateRequest;
import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.request.ProblemUpdateRequest;
import com.dao.nbti.problem.application.dto.response.*;
import com.dao.nbti.problem.domain.aggregate.*;
import com.dao.nbti.problem.domain.repository.AnswerTypeRepository;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepositoryCustom;
import com.dao.nbti.problem.exception.ProblemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProblemService {
    private final ProblemRepositoryCustom problemRepositoryCustom;
    private final ProblemRepository problemRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerTypeRepository answerTypeRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public ProblemListResponse getProblems(ProblemSearchRequest problemSearchRequest) {
        List<ProblemSummaryDTO> problems = problemRepositoryCustom.getProblemsBy(problemSearchRequest);

        long totalItems = problemRepositoryCustom.countProblemsBy(problemSearchRequest);

        return ProblemListResponse.builder()
                .problems(problems)
                .pagination(Pagination.builder()
                        .currentPage(problemSearchRequest.getPage())
                        .totalItems(totalItems)
                        .totalPage((int) Math.ceil((double) totalItems / problemSearchRequest.getSize()))
                        .build()
                ).build();
    }

    @Transactional(readOnly = true)
    public ProblemDetailsResponse getProblemDetails(int problemId) {
        Problem problem = problemRepository.findByProblemIdAndIsDeleted(problemId, IsDeleted.N)
                .orElseThrow(() -> new ProblemException(ErrorCode.PROBLEM_NOT_FOUND));
        Category childCategory = categoryRepository.findById(problem.getCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        Category parentCategory = categoryRepository.findById(childCategory.getParentCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        AnswerType answerType = answerTypeRepository.findById(problem.getAnswerTypeId())
                .orElseThrow(() -> new ProblemException(ErrorCode.ANSWER_TYPE_NOT_FOUND));

        String answerTypeDescription = AnswerTypeEnum.of(answerType.getAnswerTypeId());

        ProblemDTO problemDTO = ProblemDTO.from(problem, childCategory, parentCategory, answerTypeDescription);

        return ProblemDetailsResponse.builder()
                .problem(problemDTO)
                .build();
    }

    // 'https://a3.nbti.ai/images/problem_lang_07_01.png'
    @Transactional
    public ProblemDetailsResponse createProblem(ProblemCreateRequest request, MultipartFile imageFile) {
        int categoryId = request.getCategoryId();

        // 1. 이미지 업로드 (필수로 받는다고 가정)
        String contentImageUrl;
        try {
            contentImageUrl = s3Service.uploadProblemImage(imageFile, categoryId);
        } catch (IOException e) {
            throw new RuntimeException("S3 이미지 업로드 실패", e);
        }

        Problem problem = Problem.builder()
                .categoryId(categoryId)
                .answerTypeId(request.getAnswerTypeId())
                .contentImageUrl(contentImageUrl)
                .correctAnswer(request.getCorrectAnswer())
                .level(request.getLevel())
                .isDeleted(IsDeleted.N)
                .build();

        Problem saved = problemRepository.save(problem);

        Category child = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        Category parent = categoryRepository.findById(child.getParentCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        String answerTypeDescription = AnswerTypeEnum.of(request.getAnswerTypeId());

        return ProblemDetailsResponse.builder()
                .problem(ProblemDTO.from(saved, child, parent, answerTypeDescription))
                .build();
    }


    @Transactional
    public ProblemDetailsResponse updateProblem(ProblemUpdateRequest request, int problemId, MultipartFile imageFile) {
        Problem problem = problemRepository.findByProblemIdAndIsDeleted(problemId, IsDeleted.N)
                .orElseThrow(() -> new ProblemException(ErrorCode.PROBLEM_NOT_FOUND));

        validateProblemCommandRequest(request);

        // 이미지 새로 들어오면 S3에 업로드하고 교체
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String newImageUrl = s3Service.uploadProblemImage(imageFile, request.getCategoryId());
                problem.updateContentImageUrl(newImageUrl);
            } catch (IOException e) {
                throw new RuntimeException("문제 이미지 업로드 실패", e);
            }
        }

        // 나머지 값 수정
        problem.updateFromRequest(request);

        Category childCategory = categoryRepository.findById(problem.getCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        Category parentCategory = categoryRepository.findById(childCategory.getParentCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        String answerTypeDescription = AnswerTypeEnum.of(problem.getAnswerTypeId());

        return ProblemDetailsResponse.builder()
                .problem(ProblemDTO.from(problem, childCategory, parentCategory, answerTypeDescription))
                .build();
    }


    private void validateProblemCommandRequest(ProblemCommandRequest problemCommandRequest) {
        int categoryId = problemCommandRequest.getCategoryId();
        int answerTypeId = problemCommandRequest.getAnswerTypeId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));

        /* parentCategoryId == null일 때 발생하는 500 에러를 커스텀 에러로 전환 */
        Integer parentCategoryId = category.getParentCategoryId();
        if (parentCategoryId == null) {
            throw new ProblemException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (!categoryRepository.existsById(categoryId)) {
            throw new ProblemException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (!answerTypeRepository.existsById(answerTypeId)) {
            throw new ProblemException(ErrorCode.ANSWER_TYPE_NOT_FOUND);
        }
    }

    public ProblemDeleteResponse deleteProblem(int problemId) {
        boolean exists = problemRepository.existsByProblemIdAndIsDeleted(problemId, IsDeleted.N);
        if (!exists) {
            throw new ProblemException(ErrorCode.PROBLEM_NOT_FOUND);
        }

        problemRepository.deleteByProblemId(problemId, IsDeleted.Y);

        return ProblemDeleteResponse.builder()
                .problemId(problemId)
                .build();
    }

    public CategoryResponse getCategories() {
        List<Category> parentCategories = categoryRepository.findByParentCategoryIdIsNullOrderByCategoryIdAsc();

        List<CategorySummaryDTO> parentCategoryList = parentCategories.stream()
                .map(CategorySummaryDTO::fromCategory)
                .toList();

        List<Category> childCategories = categoryRepository.findByParentCategoryIdIsNotNullOrderByParentCategoryIdAscCategoryIdAsc();

        List<CategorySummaryDTO> childCategoryList = childCategories.stream()
                .map(CategorySummaryDTO::fromCategory)
                .toList();

        return CategoryResponse.builder()
                .parentCategories(parentCategoryList)
                .childCategories(childCategoryList)
                .build();
    }
}
