package com.dao.nbti.problem.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryResponse {
    List<CategorySummaryDTO> parentCategories;
    List<CategorySummaryDTO> childCategories;
}
