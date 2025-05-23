package com.dao.nbti.user.application.dto;

import com.dao.nbti.user.domain.aggregate.Gender;
import com.dao.nbti.user.domain.aggregate.IsDeleted;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminViewDTO {
    private String accountId;
    private String name;
    private Gender gender;
    private LocalDate birthdate;
    private int point;
    private IsDeleted isDeleted;
}
