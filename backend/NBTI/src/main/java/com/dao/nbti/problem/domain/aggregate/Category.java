package com.dao.nbti.problem.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    private Integer parentCategoryId;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private int timeLimit;
}
