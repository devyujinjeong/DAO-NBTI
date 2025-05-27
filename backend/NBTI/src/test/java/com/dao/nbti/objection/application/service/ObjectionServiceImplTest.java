package com.dao.nbti.objection.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.objection.exception.ObjectionException;
import com.dao.nbti.problem.domain.aggregate.IsDeleted;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.study.domain.repository.StudyResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import  com.dao.nbti.objection.application.dto.request.ObjectionCreateRequest;
import com.dao.nbti.objection.application.dto.response.ObjectionCreateResponse;
import com.dao.nbti.objection.domain.aggregate.Objection;
import com.dao.nbti.objection.domain.aggregate.Status;
import com.dao.nbti.objection.domain.repository.ObjectionRepository;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObjectionServiceImplTest {

    @InjectMocks
    private ObjectionServiceImpl objectionService;

    @Mock
    private ObjectionRepository objectionRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private StudyResultRepository studyResultRepository;

    @Test
    void createObjection_success() {
        // given
        int userId = 42;
        ObjectionCreateRequest request = new ObjectionCreateRequest(
                20,
                "문제의 정답이 명확하지 않습니다."
        );

        // 문제 존재 조건
        Problem dummyProblem = new Problem(); // 문제 도메인이 실제 어떤 구조인지에 따라 조정
        given(problemRepository.findById(request.getProblemId())).willReturn(Optional.of(dummyProblem));

        // 학습 결과 존재 조건
        given(studyResultRepository.existsByUserIdAndProblemId(userId, request.getProblemId()))
                .willReturn(true);

        // 저장 후 반환될 mock Objection
        Objection mockObjection = Objection.builder()
                .objectionId(16)
                .problemId(20)
                .userId(userId)
                .reason(request.getReason())
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        given(objectionRepository.save(any(Objection.class))).willReturn(mockObjection);

        // when
        ObjectionCreateResponse response = objectionService.createObjection(request, userId);
        System.out.println("saved objectionId: " + mockObjection.getObjectionId());
        System.out.println("response objectionId: " + response.getObjectionId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getObjectionId()).isEqualTo(16);
        assertThat(response.getMessage()).isEqualTo("이의 제기가 등록되었습니다.");
    }

    @Test
    void createObjection_shouldFail_whenDuplicateObjectionExists() {
        // given
        int userId = 42;
        ObjectionCreateRequest request = new ObjectionCreateRequest(20, "중복 이의 제기");

        given(problemRepository.findById(20)).willReturn(Optional.of(new Problem()));
        given(objectionRepository.existsByUserIdAndProblemId(userId, 20)).willReturn(true); // ❗ 중복 이의 제기

        // when + then
        ObjectionException ex = assertThrows(ObjectionException.class, () ->
                objectionService.createObjection(request, userId)
        );

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_OBJECTION);
    }

    @Test
    void createObjection_shouldFail_whenProblemIsDeleted() {
        // given
        int userId = 42;
        ObjectionCreateRequest request = new ObjectionCreateRequest(20, "삭제된 문제에 대한 이의 제기");

        Problem deletedProblem = mock(Problem.class);
        when(deletedProblem.getIsDeleted()).thenReturn(IsDeleted.Y);

        given(problemRepository.findById(20)).willReturn(Optional.of(deletedProblem));

        // when + then
        ObjectionException exception = assertThrows(ObjectionException.class, () ->
                objectionService.createObjection(request, userId));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PROBLEM_DELETED);
    }


    @Test
    void createObjection_shouldFail_whenUserDidNotStudyProblem() {
        // given
        int userId = 42;
        ObjectionCreateRequest request = new ObjectionCreateRequest(20, "학습하지 않은 문제");

        Problem normalProblem = mock(Problem.class);
        when(normalProblem.getIsDeleted()).thenReturn(IsDeleted.N); // 정상 상태

        given(problemRepository.findById(20)).willReturn(Optional.of(normalProblem));
        given(objectionRepository.existsByUserIdAndProblemId(userId, 20)).willReturn(false);
        given(studyResultRepository.existsByUserIdAndProblemId(userId, 20)).willReturn(false); // 학습 안 함

        // when + then
        ObjectionException exception = assertThrows(ObjectionException.class, () ->
                objectionService.createObjection(request, userId));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_OBJECTION_TARGET);
    }


}
