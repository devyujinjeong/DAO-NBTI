package com.dao.nbti.objection.application.dto.request;

import com.dao.nbti.objection.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminObjectionSearchRequest {
    private Integer page;
    private Integer size;

    private String accountId;
    private Integer problemId;
    private Status status;

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
