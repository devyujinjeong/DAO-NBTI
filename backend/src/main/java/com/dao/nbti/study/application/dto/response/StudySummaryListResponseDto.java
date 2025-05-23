package com.dao.nbti.study.application.dto.response;

import com.dao.nbti.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudySummaryListResponseDto {
    private List<StudySummaryDto> studies;
    private Pagination pagination;
}





