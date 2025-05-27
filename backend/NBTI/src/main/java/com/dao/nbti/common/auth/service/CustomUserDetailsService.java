package com.dao.nbti.common.auth.service;


import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Integer.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾지 못했습니다."));

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getUserId()),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getAuthority().name()))
        );
    }
}