package com.dao.nbti.objection.domain.aggregate;

import com.dao.nbti.objection.application.dto.request.ObjectionUpdateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "objection")
public class Objection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int objectionId;

    private Integer userId;

    @NotNull
    private int problemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @NotNull
    @NotBlank
    private String reason;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    private String information;

    public void updateFromRequest(ObjectionUpdateRequest objectionUpdateRequest) {
        Status status = objectionUpdateRequest.getStatus();
        String information = objectionUpdateRequest.getInformation();
        this.status = status;
        this.information = information;
        this.processedAt = LocalDateTime.now();
    }
}
