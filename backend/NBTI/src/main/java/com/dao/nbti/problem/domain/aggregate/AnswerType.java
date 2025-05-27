package com.dao.nbti.problem.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_type")
@Getter
@NoArgsConstructor
public class AnswerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerTypeId;
    @NotNull
    @NotBlank
    private String description;
}
