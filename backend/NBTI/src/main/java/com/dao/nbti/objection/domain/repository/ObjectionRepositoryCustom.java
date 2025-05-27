package com.dao.nbti.objection.domain.repository;

import com.dao.nbti.objection.application.dto.request.AdminObjectionSearchRequest;
import com.dao.nbti.objection.application.dto.response.AdminObjectionDTO;

import java.util.List;

public interface ObjectionRepositoryCustom {
    List<AdminObjectionDTO> getObjections(AdminObjectionSearchRequest request);

    long countObjectionsBy(AdminObjectionSearchRequest request);
}
