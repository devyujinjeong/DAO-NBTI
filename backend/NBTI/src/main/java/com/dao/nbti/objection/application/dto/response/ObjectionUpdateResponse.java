package com.dao.nbti.objection.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ObjectionUpdateResponse {
    private int objectionId;
    private String message;
}
