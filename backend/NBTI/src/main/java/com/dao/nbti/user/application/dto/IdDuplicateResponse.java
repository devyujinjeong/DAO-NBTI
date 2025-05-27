package com.dao.nbti.user.application.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IdDuplicateResponse {
    boolean isDuplicate;
}
