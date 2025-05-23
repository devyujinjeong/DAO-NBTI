package com.dao.nbti.test.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TestSubmitRequest {

    // 비회원이 새로 고침 했을 때, 저장하기 위한 부분
    private String guestId;

    @NotNull(message = "제출된 답안은 null일 수 없습니다.")
    @Valid
    private List<TestAnswerDTO> answers;

}
