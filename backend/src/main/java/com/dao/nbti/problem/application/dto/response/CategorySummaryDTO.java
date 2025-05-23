package com.dao.nbti.problem.application.dto.response;

import com.dao.nbti.problem.domain.aggregate.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategorySummaryDTO {
    private int categoryId;
    private Integer parentCategoryId;
    private String name;

    @Builder
    public CategorySummaryDTO(int categoryId, Integer parentCategoryId, String name) {
        this.categoryId = categoryId;
        this.parentCategoryId = parentCategoryId;
        this.name = name;
    }

    public static CategorySummaryDTO fromCategory(Category category) {
        return CategorySummaryDTO.builder()
                .categoryId(category.getCategoryId())
                .parentCategoryId(category.getParentCategoryId())
                .name(category.getName())
                .build();
    }
}
