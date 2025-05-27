package com.dao.nbti.test.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.domain.aggregate.AnswerType;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.AnswerTypeRepository;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemTestRepository;
import com.dao.nbti.study.exception.NoSuchAnswerTypeException;
import com.dao.nbti.study.exception.NoSuchCategoryException;
import com.dao.nbti.study.exception.ProblemNotFoundException;
import com.dao.nbti.test.application.dto.request.TestAnswerDTO;
import com.dao.nbti.test.application.dto.response.*;
import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.application.dto.request.TestSubmitRequest;
import com.dao.nbti.test.domain.aggregate.IsCorrect;
import com.dao.nbti.test.domain.aggregate.TestProblem;
import com.dao.nbti.test.domain.aggregate.TestResult;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import com.dao.nbti.test.exception.TestException;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    private final TestResultRepository testResultRepository;
    private final com.dao.nbti.test.domain.repository.TestProblemRepository testProblemRepository;
    private final ProblemTestRepository problemTestRepository;
    private final TestAiAnswerService testAiAnswerService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerTypeRepository answerTypeRepository;
    private final RedisTemplate<String, Object> problemRedisTemplate;

    /* 검사 문제 제공하기 */
    @Transactional(readOnly = true)
    public TestProblemListResponse getTestProblems(Integer userId) {
        // 1. 문제를 담아서 반환하는 list 선언
        List<TestResponse> problemList = new ArrayList<>();

        // 2. 비회원인 경우에는 맛보기 검사 제공 (분야별 1개씩 랜덤으로 문제 제공)
        if (userId == null) {
            for(int i=1; i<=6; i++) {
                Problem problem = problemTestRepository.findSampleProblemByParentCategory(i)
                        .orElseThrow(() -> new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));

                problemList.add(TestResponseDto(problem));
            }
        } else {
            // userId가 있는 경우에는 18문제를 제공 (분야별 + 레벨별로 문제 제공)
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

            /* 포인트가 5보다 적은 경우에 예외 발생*/
            if(user.getPoint() < 5) {
                throw new TestException(ErrorCode.NOT_ENOUGH_POINT);
            }

            for(int i=1; i<=6; i++) { // 카테고리
                for(int j=1; j<=3; j++) { // 레벨
                    Problem problem = problemTestRepository.findFormalProblemByParentCategoryAndLevel(i, j)
                            .orElseThrow(() -> new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));

                    problemList.add(TestResponseDto(problem));
                }
            }
        }

        // 문제가 랜덤으로 생성되기 때문에 새로고침 할 때마다 문제가 다시 가져와진다는 문제가 생긴다.
        // 30분 동안은 문제를 지속할 수 있게 설정하기 위해 redis에 문제를 저장한다.

        String guestId = null;
        String key;

        if (userId != null) {
            key = "test:problems:" + userId;
        } else {
            guestId = UUID.randomUUID().toString();
            key = "test:problems:" + guestId;
        }

        problemRedisTemplate.opsForValue().set(key, problemList, Duration.ofMinutes(30));

        return TestProblemListResponse.builder()
                .problemList(problemList)
                .guestId(guestId)
                .build();
    }

    /* 검사 채점 하고 문제와 그 결과 저장하기 */
    @Transactional
    public int submitTest(TestSubmitRequest request, Integer userId) {

        // 회원인 경우에는 포인트 차감하기 (point 검사는 위에서 수행했음)
        if(userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

            user.usePoint();
        }

        // 문제 리스트 가져오기
        String key = (userId != null)
                ? "test:problems:" + userId
                : "test:problems:" + request.getGuestId();

        List<TestResponse> problems =
                (List<TestResponse>) problemRedisTemplate.opsForValue().get(key);

        log.info("{}",problems);

        if (problems == null) {
            throw new IllegalStateException("검사 문제가 만료되었거나 존재하지 않습니다.");
        }

        // 문제 답 매핑하기
        Map<Integer, String> userAnswers = request.getAnswers().stream()
                .collect(Collectors.toMap(
                        TestAnswerDTO::getProblemId,
                        TestAnswerDTO::getAnswer
                ));

        // 문제 채점해 결과 생성하기
        TestResultCreateRequest scoreResult = calculateScore(key, userAnswers);

        // 결과 저장
        int resultId = createTestResult(scoreResult, userId);

        // 출제 문제 저장
        for (TestResponse testResponse : problems) {
            String userAnswer = userAnswers.get(testResponse.getProblemId());

            IsCorrect isCorrect;

            if (userAnswer == null || userAnswer.trim().isEmpty()) {
                isCorrect = IsCorrect.N;
            } else if (userAnswer.equals(testResponse.getCorrectAnswer())) {
                isCorrect = IsCorrect.Y;
            } else {
                isCorrect = IsCorrect.N;
            }

            testProblemRepository.save(TestProblem.builder()
                    .testResultId(resultId)
                    .problemId(testResponse.getProblemId())
                    .answer(userAnswer)
                    .isCorrect(isCorrect)
                    .build());
        }

        // 캐시 삭제
        problemRedisTemplate.delete(key);

        return resultId;
    }


    // 문제를 응답하는 메서드
    private TestResponse TestResponseDto(Problem problem) {
        // 존재 하지 않는 카테고리나 응답 인 경우에 예외 발생 처리하기
        Category category = categoryRepository.findById(problem.getCategoryId())
                .orElseThrow(() -> new NoSuchCategoryException(ErrorCode.CATEGORY_NOT_FOUND));

        AnswerType answerType = answerTypeRepository.findById(problem.getAnswerTypeId())
                .orElseThrow(() -> new NoSuchAnswerTypeException(ErrorCode.ANSWER_TYPE_NOT_FOUND));

        return TestResponse.builder()
                .problemId(problem.getProblemId())
                .categoryId(problem.getCategoryId())
                .categoryName(category.getName())
                .answerTypeId(problem.getAnswerTypeId())
                .answerTypeDescription(answerType.getDescription())
                .contentImageUrl(problem.getContentImageUrl())
                .level(problem.getLevel())
                .timeLimit(category.getTimeLimit())
                .correctAnswer(problem.getCorrectAnswer())
                .build();
    }

    /* 검사 결과 생성하고 저장하기*/
    private int createTestResult(TestResultCreateRequest request, Integer userId) {

        /* ai 분석 결과*/
        String aiText = testAiAnswerService.createAiAnswer(request, userId);

        /* 검사 결과 생성하기 */
        TestResult testResult = TestResult.builder()
                .userId(userId)
                .langComp(request.getLangComp())
                .percReason(request.getPercReason())
                .generalKnowledge(request.getGeneralKnowledge())
                .procSpeed(request.getProcSpeed())
                .spatialPerception(request.getSpatialPerception())
                .workMemory(request.getWorkMemory())
                .createdAt(LocalDateTime.now())
                .aiText(aiText)
                .build();

        /* 검사 결과 저장하기*/
        testResultRepository.save(testResult);

        return testResult.getTestResultId();
    }


    // 문제를 채점해 결과를 생성하는 메서드
    private TestResultCreateRequest calculateScore(
            String key,
            Map<Integer, String> userAnswers
    ) {
        List<TestResponse> problems =
                (List<TestResponse>) problemRedisTemplate.opsForValue().get(key);

        log.info("Redis 저장 문제: {}", problems);

        if (problems == null) {
            throw new IllegalStateException("검사 문제가 만료되었거나 존재하지 않습니다.");
        }

        int lang = 0, general = 0, perc = 0, work = 0, proc = 0, spatial = 0;

        for (TestResponse problem : problems) {
            String userAnswer = userAnswers.get(problem.getProblemId());

            // 답을 체크하지 않은 경우에는 채점 0점으로 처리함
            if (userAnswer == null || userAnswer.trim().isEmpty()) {
                continue;
            }

            // 하위 카테고리 id를 통해 상위 카테고리 id 가져오기
            int categoryId = problem.getCategoryId();

            if (userAnswer.equals(problem.getCorrectAnswer())) {
                // 해당 문제의 부모 카테고리 id를 가져오기
                int parentCategoryId = categoryRepository.findParentCategoryIdByCategoryId(categoryId);

                // 점수는 Level 에 맞게 저장하기
                int score = problem.getLevel();

                switch (parentCategoryId) {
                    case 1 -> lang += score;
                    case 2 -> general += score;
                    case 3 -> perc += score;
                    case 4 -> work += score;
                    case 5 -> proc += score;
                    case 6 -> spatial += score;
                }
            }
        }

        return TestResultCreateRequest.builder()
                .langComp(lang)
                .generalKnowledge(general)
                .percReason(perc)
                .workMemory(work)
                .procSpeed(proc)
                .spatialPerception(spatial)
                .build();
    }

    @Transactional(readOnly = true)
    public TestResultResponse getTestResult(int testResultId){

        TestResult testResult = testResultRepository.findByTestResultId(testResultId)
                .orElseThrow(() -> new TestException(ErrorCode.TEST_RESULT_NOT_FOUND));

        return toDetailResponse(testResult, testResult.getUserId());
    }

    private TestResultResponse toDetailResponse(TestResult result, Integer userId) {
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

        return TestResultResponse.builder()
                .userId(userId)
                .scores(scores)
                .aiText(result.getAiText())
                .build();
    }

    private CategoryScoreDetail newScoreDetail(String name, int score, Map<String, String> descMap) {
        return CategoryScoreDetail.builder()
                .categoryName(name)
                .description(descMap.getOrDefault(name, ""))
                .score(score)
                .build();
    }

    /* 마이페이지에 검사 결과 저장하기*/
    @Transactional
    public void updateTestResult(Integer userId, int testResultId) {

        // userId 가져오기 (로그인 된 user의 Id)
        // user가 있는지 확인하기
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        log.info("userId: {}", user.getUserId());

        // 검사 결과 가져오기
        TestResult testResult = testResultRepository.findById(testResultId)
                .orElseThrow(() -> new TestException(ErrorCode.TEST_RESULT_NOT_FOUND));

        // 본인의 검사 결과인지 확인하기
        if (!testResult.getUserId().equals(userId)) {
            log.info("본인의 검사 결과가 아닙니다. " );
            throw new TestException(ErrorCode.NOT_YOUR_RESULT);
        }

        // 검사 결과 수정하기
        testResult.saveToMyPage();
    }

}