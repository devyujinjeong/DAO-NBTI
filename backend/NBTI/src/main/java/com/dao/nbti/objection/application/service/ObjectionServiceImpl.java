package com.dao.nbti.objection.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.objection.application.dto.request.ObjectionCreateRequest;
import com.dao.nbti.objection.application.dto.response.ObjectionCreateResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionDetailResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.domain.aggregate.Objection;
import com.dao.nbti.objection.domain.aggregate.Status;
import com.dao.nbti.objection.domain.repository.ObjectionRepository;
import com.dao.nbti.objection.exception.ObjectionException;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.IsDeleted;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.study.domain.repository.StudyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjectionServiceImpl implements ObjectionService {

    private final ObjectionRepository objectionRepository;
    private final ProblemRepository problemRepository;
    private final StudyResultRepository studyResultRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ObjectionSummaryResponse> getObjectionsByUser(int userId, Status status, Integer parentCategoryId, Pageable pageable) {
        Page<Objection> objections = (status == null)
                ? objectionRepository.findByUserId(userId, pageable)
                : objectionRepository.findByUserIdAndStatus(userId, status, pageable);

        return objections
                .stream()
                .filter(objection -> {
                    if (parentCategoryId == null) return true;

                    Problem problem = problemRepository.findById(objection.getProblemId())
                            .orElseThrow(() -> new ObjectionException(ErrorCode.PROBLEM_NOT_FOUND));

                    Category subCategory = categoryRepository.findById(problem.getCategoryId())
                            .orElseThrow(() -> new ObjectionException(ErrorCode.CATEGORY_NOT_FOUND));

                    return parentCategoryId.equals(subCategory.getParentCategoryId());
                })
                .map(objection -> {
                    Problem problem = problemRepository.findById(objection.getProblemId()).get();
                    Category subCategory = categoryRepository.findById(problem.getCategoryId()).get();
                    Category parentCategory = categoryRepository.findById(subCategory.getParentCategoryId()).get();

                    return ObjectionSummaryResponse.builder()
                            .objectionId(objection.getObjectionId())
                            .problemId(objection.getProblemId())
                            .reason(objection.getReason())
                            .parentCategoryName(parentCategory.getName())
                            .status(objection.getStatus())
                            .createdAt(objection.getCreatedAt())
                            .build();
                })
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    int start = (int) pageable.getOffset();
                    int end = Math.min((start + pageable.getPageSize()), list.size());
                    return new org.springframework.data.domain.PageImpl<>(list.subList(start, end), pageable, list.size());
                }));
    }



    @Override
    public ObjectionDetailResponse getObjectionDetail(int objectionId, int userId) {
        Objection objection = objectionRepository.findByObjectionIdAndUserId(objectionId, userId)
                .orElseThrow(() -> new ObjectionException(ErrorCode.UNAUTHORIZED_TEST_RESULT_ACCESS)); // or OBJECTION_NOT_FOUND

        return ObjectionDetailResponse.builder()
                .objectionId(objection.getObjectionId())
                .problemId(objection.getProblemId())
                .status(objection.getStatus())
                .reason(objection.getReason())
                .information(objection.getInformation())
                .createdAt(objection.getCreatedAt())
                .processedAt(objection.getProcessedAt())
                .build();
    }

    @Override
    public ObjectionCreateResponse createObjection(ObjectionCreateRequest request, int userId) {
        int problemId = request.getProblemId();

        // 1. 문제 존재 여부 및 삭제 여부 확인
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ObjectionException(ErrorCode.PROBLEM_NOT_FOUND));

        if (problem.getIsDeleted() == IsDeleted.Y) {
            throw new ObjectionException(ErrorCode.PROBLEM_DELETED);
        }

        // 2. 중복 이의 제기 방지
        if (objectionRepository.existsByUserIdAndProblemId(userId, problemId)) {
            throw new ObjectionException(ErrorCode.DUPLICATE_OBJECTION);
        }

        // 3. 학습한 문제인지 확인
        if (!studyResultRepository.existsByUserIdAndProblemId(userId, problemId)) {
            throw new ObjectionException(ErrorCode.INVALID_OBJECTION_TARGET);
        }

        // 4. 저장
        Objection objection = Objection.builder()
                .userId(userId)
                .problemId(problemId)
                .status(Status.PENDING)
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .build();

        Objection saved = objectionRepository.save(objection);

        return ObjectionCreateResponse.builder()
                .objectionId(saved.getObjectionId())
                .message("이의 제기가 등록되었습니다.")
                .build();
    }
}



