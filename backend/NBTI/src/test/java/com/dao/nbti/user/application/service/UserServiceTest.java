package com.dao.nbti.user.application.service;

import com.dao.nbti.user.application.dto.IdDuplicateResponse;
import com.dao.nbti.user.application.dto.UserCreateRequest;
import com.dao.nbti.user.application.dto.UserDeleteRequest;
import com.dao.nbti.user.domain.aggregate.Authority;
import com.dao.nbti.user.domain.aggregate.Gender;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User user;
    private final int userId = 1;
    private final String accountId = "user01";
    private final String password = "encodedPassword";

    @BeforeEach
    void setUp(){
        user = User.builder()
                .userId(1)
                .accountId(accountId)
                .password(password)
                .name("홍길동")
                .gender(Gender.M)
                .birthdate(LocalDate.now())
                .authority(Authority.USER)
                .build();
    }


    @Test
    void createUser() {
        user.setPassword("password01@");
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .accountId("user01")
                .password("password01@")
                .name("홍길동")
                .gender(Gender.M)
                .birthdate(LocalDate.now())
                .build();
        when(modelMapper.map(userCreateRequest, User.class)).thenReturn(user);
        when(userRepository.findByAccountId("user01")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password01@")).thenReturn("encodedPassword");
        userService.createUser(userCreateRequest);
    }

    @Test
    void createUser_loginIdAlreadyExist(){
        user.setPassword("password01@");
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .accountId("user01")
                .password("password01@")
                .name("홍길동")
                .gender(Gender.M)
                .birthdate(LocalDate.now())
                .build();

        when(modelMapper.map(userCreateRequest, User.class)).thenReturn(user);
        when(userRepository.findByAccountId("user01")).thenReturn(Optional.of(user));

        assertThrows(UserException.class, () ->userService.createUser(userCreateRequest));

    }

    @Test
    void deleteUser() {
        UserDeleteRequest request = UserDeleteRequest.builder()
                .password("password01@")
                .build();
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("password01@")).thenReturn("encodedPassword");
        when(passwordEncoder.matches(any(),any())).thenReturn(true);
        userService.deleteUser(userId,request);
    }

    @Test
    void deleteUser_userNotFound(){
        UserDeleteRequest request = UserDeleteRequest.builder()
                .password("password01@")
                .build();
        when(userRepository.findByUserIdAndDeletedAtIsNull(userId)).thenReturn(Optional.empty());

        assertThrows(UserException.class ,()-> userService.deleteUser(userId,request));
    }

    @Test
    void checkAccountId_isDuplicate() {
        when(userRepository.findByAccountId(accountId)).thenReturn(Optional.of(user));

        IdDuplicateResponse response = userService.checkAccountId(accountId);

        assertTrue(response.isDuplicate());
    }

    @Test
    void checkAccountId_isNotDuplicate() {
        when(userRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        IdDuplicateResponse response = userService.checkAccountId(accountId);

        assertFalse(response.isDuplicate());
    }

}