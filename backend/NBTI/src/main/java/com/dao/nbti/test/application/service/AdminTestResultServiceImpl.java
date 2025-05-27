package com.dao.nbti.test.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.test.application.dto.request.AdminTestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.*;
import com.dao.nbti.test.domain.aggregate.TestResult;
import com.dao.nbti.test.domain.repository.TestRepositoryCustom;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import com.dao.nbti.test.exception.TestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AdminTestResultServiceImpl implements AdminTestResultService{
    private final TestResultRepository testResultRepository;
    private final CategoryRepository categoryRepository;
    private final TestRepositoryCustom testRepositoryCustom;
    private final ModelMapper modelMapper;


    @Override
    public AdminTestResultSummaryResponse getAdminTestResultList(AdminTestResultSearchCondition condition, Pageable pageable) {
        List<AccountTestResultDto> results = testRepositoryCustom.getTest(condition,pageable);
        List<AdminTestResultSummaryDTO> content = results.stream()
                .map(dto -> {
                    AdminTestResultSummaryDTO adminTestResultSummaryDTO= modelMapper.map(
                            toSummaryResponse(dto.getTestResult()), AdminTestResultSummaryDTO.class
                    );
                    adminTestResultSummaryDTO.setAccountId(dto.getAccountId());
                    return adminTestResultSummaryDTO;
                }).toList();
        long total = testRepositoryCustom.countTest(condition);
        return new AdminTestResultSummaryResponse(new PageImpl<>(content, pageable, total));
    }

    @Override
    public TestResultDetailResponse getTestResultDetail(int testResultId) {
        TestResult testResult = testResultRepository.findByTestResultId(testResultId)
                .orElseThrow(() -> new TestException(ErrorCode.TEST_RESULT_NOT_FOUND));

        return toDetailResponse(testResult);
    }

    private TestResultSummaryResponse toSummaryResponse(TestResult result) {
        int[] scores = {
                result.getLangComp(),
                result.getGeneralKnowledge(),
                result.getPercReason(),
                result.getWorkMemory(),
                result.getProcSpeed(),
                result.getSpatialPerception()
        };
        String[] categories = {
                "언어 이해", "시사 상식", "지각 추론", "작업 기억", "처리 속도", "공간 지각력"
        };

        int maxIdx = 0, minIdx = 0;
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > scores[maxIdx]) maxIdx = i;
            if (scores[i] < scores[minIdx]) minIdx = i;
        }

        int totalScore = (int) Math.round(
                (scores[0] + scores[1] + scores[2] + scores[3] + scores[4] + scores[5]) / 6.0
        );

        return TestResultSummaryResponse.builder()
                .testResultId(result.getTestResultId())
                .createdAt(result.getCreatedAt())
                .highestCategory(categories[maxIdx])
                .lowestCategory(categories[minIdx])
                .totalScore(totalScore)
                .build();
    }

    private TestResultDetailResponse toDetailResponse(TestResult result) {
        List<String> categoryNames = List.of(
                "언어 이해", "시사 상식", "지각 추론", "작업 기억", "처리 속도", "공간 지각력"
        );

        Map<String, String> descriptionMap = categoryRepository.findAll().stream()
                .filter(c -> c.getParentCategoryId() == null && categoryNames.contains(c.getName()))
                .collect(Collectors.toMap(Category::getName, Category::getDescription));

        List<CategoryScoreDetail> scores = List.of(
                newScoreDetail("언어 이해", result.getLangComp(), descriptionMap),
                newScoreDetail("시사 상식", result.getGeneralKnowledge(), descriptionMap),
                newScoreDetail("지각 추론", result.getPercReason(), descriptionMap),
                newScoreDetail("작업 기억", result.getWorkMemory(), descriptionMap),
                newScoreDetail("처리 속도", result.getProcSpeed(), descriptionMap),
                newScoreDetail("공간 지각력", result.getSpatialPerception(), descriptionMap)
        );

        return TestResultDetailResponse.builder()
                .scores(scores)
                .aiText(result.getAiText())
                .createdAt(result.getCreatedAt())
                .build();
    }

    private CategoryScoreDetail newScoreDetail(String name, int score, Map<String, String> descMap) {
        return CategoryScoreDetail.builder()
                .categoryName(name)
                .description(descMap.getOrDefault(name, ""))
                .score(score)
                .build();
    }
}
