package com.dao.nbti.test.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.exception.TestException;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*
* 지능 검사 결과를 요청 후 응답 받는 로직이 작성된 서비스 클래스
* */
@Slf4j
@Service
public class TestAiAnswerServiceImpl implements TestAiAnswerService{

    // 메인 진입점 클래스
    // OpenAI의 API 서버에 요청을 보내고, 응답을 받아오는 클라이언트
    private final OpenAIClient client;

    public TestAiAnswerServiceImpl(@Value("${openai.api-key}") String apiKey) {
        this.client = OpenAIOkHttpClient.builder()
                        .apiKey(apiKey)
                        .build();
    }

    /* 응답 생성 */
    @Override
    public String createAiAnswer(TestResultCreateRequest request, Integer userId) {

        // 프롬포트 생성해서 넘겨 주기
        String prompt = createPrompt(request, userId);

        log.info(prompt);

        // 요청 전달하기
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO) // 어떤 모델을 사용할지 지정하기
                .addUserMessage(prompt) // GPt에게 입력하는 질문
                .temperature(0) // 창의성 조절 값 (0~1로 1에 가까울 수록 창의적임)
                .build();


        // 응답 결과 객체 : 채팅 기반 모델을 사용해 메시지 기반으로 응답 (AI 호출하기)
        try {
            ChatCompletion completion = client.chat().completions().create(params);
            return completion.choices().get(0).message().content().orElse("").trim();
        } catch (Exception e) {
            // ai 호출 시 발생하는 에러
            throw new TestException(ErrorCode.AI_CALL_FAILED);
        }
    }

    /* 프롬포트 만들기 */
    private String createPrompt(TestResultCreateRequest request, Integer userId) {
        String scoreRange = (userId != null) ? "0점 ~ 6점까지 입니다." : "0점 ~ 2점까지 입니다.";

        return String.format("""
                다음은 사용자의 지능 검사 결과입니다.
                각각의 검사별로 점수는 %s
                        
                - 언어 이해: %d점
                - 시사 상식: %d점
                - 지각 추론: %d점
                - 작업 기억: %d점
                - 처리 속도: %d점
                - 공간 지각력: %d점

                위 정보를 바탕으로 사용자의 전반적인 인지 능력, 성향, 특징 등을 총평해주세요.
                모든 총평은 무조건 250글자 이내로 서술해주세요.
                말투는 전문가가 정중하게 해설하는 듯한 느낌으로, 너무 가볍지 않게 써주세요.
                그리고 정보를 제공할 때, '당신은'이라는 말을 이용해서 설명해주세요.
                """,
                scoreRange,
                request.getLangComp(),
                request.getGeneralKnowledge(),
                request.getPercReason(),
                request.getWorkMemory(),
                request.getProcSpeed(),
                request.getSpatialPerception()
        );
    }
}
