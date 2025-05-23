package com.dao.nbti.study.application.dto.response;

import com.dao.nbti.problem.domain.aggregate.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyCategoryListResponseDto {
    private List<CategoryDto> categories;

    @Getter
    @Builder
    public static class CategoryDto {
        private int categoryId;
        private String name;
        private String description;
        private int timeLimit;

        public static CategoryDto from(Category category) {
            return CategoryDto.builder()
                    .categoryId(category.getCategoryId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .timeLimit(category.getTimeLimit())
                    .build();
        }
    }

    public static StudyCategoryListResponseDto from(List<Category> categories) {
        List<CategoryDto> dtoList = categories.stream()
                .map(CategoryDto::from)
                .toList();

        return StudyCategoryListResponseDto.builder()
                .categories(dtoList)
                .build();
    }


}
