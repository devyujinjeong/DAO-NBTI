package com.dao.nbti.objection.application.service;

import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.objection.application.dto.request.AdminObjectionSearchRequest;
import com.dao.nbti.objection.application.dto.request.ObjectionUpdateRequest;
import com.dao.nbti.objection.application.dto.response.*;
import com.dao.nbti.objection.domain.aggregate.Objection;
import com.dao.nbti.objection.domain.aggregate.Status;
import com.dao.nbti.objection.domain.repository.ObjectionRepository;
import com.dao.nbti.objection.domain.repository.ObjectionRepositoryCustom;
import com.dao.nbti.objection.exception.ObjectionException;
import com.dao.nbti.user.domain.aggregate.IsDeleted;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminObjectionService {
    private final ObjectionRepositoryCustom objectionRepositoryCustom;
    private final ObjectionRepository objectionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public AdminObjectionListResponse getObjections(AdminObjectionSearchRequest adminObjectionSearchRequest) {
        List<AdminObjectionDTO> objections = objectionRepositoryCustom.getObjections(adminObjectionSearchRequest);

        long totalItems = objectionRepositoryCustom.countObjectionsBy(adminObjectionSearchRequest);

        return AdminObjectionListResponse.builder()
                .objections(objections)
                .pagination(Pagination.builder()
                                .currentPage(adminObjectionSearchRequest.getPage())
                                .totalItems(totalItems)
                                .totalPage((int) Math.ceil((double) totalItems / adminObjectionSearchRequest.getSize()))
                                .build())
                .build();
    }

    @Transactional(readOnly = true)
    public AdminObjectionDetailResponse getObjectionDetails(int objectionId) {
        Objection objection = objectionRepository.findById(objectionId)
                .orElseThrow(() -> new ObjectionException(ErrorCode.OBJECTION_NOT_FOUND));

        Integer userId = objection.getUserId();
        AdminObjectionDetailsDTO objectionDetails;

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

            objectionDetails = AdminObjectionDetailsDTO.from(objection, user.getAccountId());
            if (user.getIsDeleted() == IsDeleted.Y) {
                objectionDetails = AdminObjectionDetailsDTO.from(objection, "삭제된 유저");
            }
        }
        else {
            objectionDetails = AdminObjectionDetailsDTO.from(objection, "삭제된 유저");
        }

        return AdminObjectionDetailResponse.builder()
                .objectionDetails(objectionDetails)
                .build();
    }

    @Transactional
    public ObjectionUpdateResponse updateObjection(int objectionId, ObjectionUpdateRequest objectionUpdateRequest) {
        Objection objection = objectionRepository.findById(objectionId)
                .orElseThrow(() -> new ObjectionException(ErrorCode.OBJECTION_NOT_FOUND));

        Status status = objection.getStatus();
        if (status != Status.PENDING) {
            throw new ObjectionException(ErrorCode.OBJECTION_ALREADY_UPDATED);
        }
        validateUpdateRequest(objectionUpdateRequest);

        objection.updateFromRequest(objectionUpdateRequest);

        String message = "이의 제기가 %s되었습니다.";
        Status requestedStatus = objection.getStatus();
        switch (requestedStatus) {
            // PENDING이면 검증 메서드에서 예외 처리됨
            case ACCEPTED -> message = message.formatted("승인");
            case REJECTED -> message = message.formatted("반려");
        }

        return ObjectionUpdateResponse.builder()
                .objectionId(objectionId)
                .message(message)
                .build();
    }

    private void validateUpdateRequest(ObjectionUpdateRequest objectionUpdateRequest) {
        Status requestedStatus = objectionUpdateRequest.getStatus();
        String information = objectionUpdateRequest.getInformation();

        if (requestedStatus == Status.PENDING) {
            throw new ObjectionException(ErrorCode.INVALID_STATUS);
        }
        if (requestedStatus == Status.REJECTED && information.isBlank()) {
            throw new ObjectionException(ErrorCode.REJECTION_REASON_REQUIRED);
        }
    }
}
