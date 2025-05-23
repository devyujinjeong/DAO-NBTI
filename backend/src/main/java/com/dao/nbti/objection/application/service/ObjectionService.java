package com.dao.nbti.objection.application.service;

import com.dao.nbti.objection.application.dto.request.ObjectionCreateRequest;
import com.dao.nbti.objection.application.dto.response.ObjectionCreateResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionDetailResponse;
import com.dao.nbti.objection.domain.aggregate.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ObjectionService {

    // 목록 조회
    Page<ObjectionSummaryResponse> getObjectionsByUser(int userId, Status status, Integer parentCategoryId, Pageable pageable);

    // 상세 조회
    ObjectionDetailResponse getObjectionDetail(int objectionId, int userId);

    // 이의제기
    ObjectionCreateResponse createObjection(ObjectionCreateRequest request, int userId);
}
