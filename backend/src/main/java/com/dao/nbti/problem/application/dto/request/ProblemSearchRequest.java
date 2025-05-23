package com.dao.nbti.problem.application.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProblemSearchRequest {
    private Integer page;
    private Integer size;

    Integer parentCategoryId;
    Integer childCategoryId;
    Integer level;

    public int getOffset() {
        return (getPage() - 1) * getSize();
    }

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