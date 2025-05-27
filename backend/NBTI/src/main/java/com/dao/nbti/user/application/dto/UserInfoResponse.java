package com.dao.nbti.user.application.dto;

import com.dao.nbti.user.domain.aggregate.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private String accountId;
    private String name;
    private Gender gender;
    private LocalDate birthdate;
    private int point;
}
