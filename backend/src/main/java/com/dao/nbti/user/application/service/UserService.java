package com.dao.nbti.user.application.service;



import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.user.application.dto.*;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.domain.repository.UserRepositoryCustom;
import com.dao.nbti.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryCustom userRepositoryCustom;

    @Transactional
    public void createUser(UserCreateRequest userCreateRequest) {
        User user = modelMapper.map(userCreateRequest, User.class);
        if(userRepository.findByAccountId(user.getAccountId()).isPresent()){
            throw new UserException(ErrorCode.LOGIN_ID_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        user.delete();
    }

    @Transactional
    public IdDuplicateResponse checkAccountId(String accountId) {
        boolean isDuplicate = userRepository.findByAccountId(accountId).isPresent();

        return IdDuplicateResponse.builder()
                .isDuplicate(isDuplicate)
                .build();
    }

    public UserInfoResponse getUserInfo(String username) {
        int userId = Integer.parseInt(username);
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId).orElseThrow(
                () -> new UserException(ErrorCode.USER_NOT_FOUND)
        );

        UserInfoResponse response = modelMapper.map(user, UserInfoResponse.class);
        return response;
    }

    public UserAdminViewResponse getUserList(UserSearchCondition condition, Pageable pageable) {
        List<User> users = userRepositoryCustom.getUsers(
                condition,
                pageable
        );

        List<UserAdminViewDTO> content = users.stream()
                .map(user -> modelMapper.map(user, UserAdminViewDTO.class))
                .collect(Collectors.toList());

        long total = userRepositoryCustom.countUsers(condition); // 조건에 맞는 전체 유저 수

        return new UserAdminViewResponse(new PageImpl<>(content, pageable, total));

    }

    @Transactional(readOnly = true)
    public int getUserPoint(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return user.getPoint();
    }
}
