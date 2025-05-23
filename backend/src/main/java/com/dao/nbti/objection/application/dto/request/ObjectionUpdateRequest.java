package com.dao.nbti.objection.application.dto.request;

import com.dao.nbti.objection.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ObjectionUpdateRequest {
    private Status status;
    private String information;
}
