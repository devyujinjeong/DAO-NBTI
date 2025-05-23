package com.dao.nbti.user.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pointId;

    @Column
    private Integer userId; // 외래키지만 연관관계는 맺지 않음

    @Column(nullable = false)
    private int point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType pointType;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
