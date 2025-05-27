package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.response.TestResultResponse;
import com.dao.nbti.test.domain.aggregate.TestResult;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import com.dao.nbti.user.domain.aggregate.Authority;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {
    @Mock
    private TestResultRepository testResultRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TestServiceImpl testService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId(1)
                .accountId("userId0001")
                .password("encodedPassword")
                .authority(Authority.USER)
                .point(10)
                .build();
    }

    @Test
    @DisplayName("검사 결과 조회하기 테스트")
    void getTestResultTest() {
        TestResult testResult = TestResult.builder()
                .userId(1)
                .langComp(5)
                .percReason(5)
                .generalKnowledge(5)
                .procSpeed(5)
                .spatialPerception(5)
                .workMemory(5)
                .aiText("AI 분석")
                .build();

        when(testResultRepository.findByTestResultId(1)).thenReturn(Optional.of(testResult));

        TestResultResponse response = testService.getTestResult(1);

        assertThat(response).isNotNull();
        assertThat(response.getAiText()).isEqualTo("AI 분석");
    }

    @Test
    @DisplayName("검사 결과 마이페이지에 저장하기 테스트")
    void saveTestResultToMyPage() {
        TestResult testResult = TestResult.builder()
                .userId(1)
                .build();

        when(userRepository.findByUserId(1)).thenReturn(Optional.of(testUser));
        when(testResultRepository.findById(1)).thenReturn(Optional.of(testResult));

        testService.updateTestResult(1, 1);

        verify(testResultRepository, times(1)).findById(1);
    }
}