package com.dao.nbti.objection.application.dto.response;

import com.dao.nbti.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminObjectionListResponse {
    private List<AdminObjectionDTO> objections;
    private Pagination pagination;
}
