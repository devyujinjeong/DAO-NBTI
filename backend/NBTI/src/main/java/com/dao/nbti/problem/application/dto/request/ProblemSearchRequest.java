package com.dao.nbti.problem.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProblemSearchRequest {
    @Schema(description = "조회 페이지", example="2")
    private Integer page;
    @Schema(description = "페이지 당 항목 수", example="10")
    private Integer size;

    @Schema(description = "상위 분야 ID", example="1")
    Integer parentCategoryId;
    @Schema(description = "하위 분야 ID", example="7")
    Integer childCategoryId;
    @Schema(description = "난이도", example="1")
    Integer level;

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