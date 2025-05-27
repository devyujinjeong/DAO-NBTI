package com.dao.nbti.objection.application.dto.request;

import com.dao.nbti.objection.domain.aggregate.Status;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminObjectionSearchRequest {
    @Schema(description = "조회 페이지", example="2")
    private Integer page;
    @Schema(description = "페이지 당 항목 수", example="10")
    private Integer size;

    @Schema(description = "회원 계정 ID", example="user0001")
    private String accountId;
    @Schema(description = "문제 ID", example = "10")
    private Integer problemId;
    @Schema(description = "처리 상태", example = "PENDING")
    private Status status;

    @Schema(hidden = true)
    public int getOffset() {
        return (getPage() - 1) * getSize();
    }
    @Schema(hidden = true)
    public int getLimit() {
        return getSize();
    }

    public int getPage() {
        return page != null ? page : 1;
    }

    public int getSize() {
        return size != null ? size : 10;
    }
}
