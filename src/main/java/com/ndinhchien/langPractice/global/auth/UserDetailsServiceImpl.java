package com.ndinhchien.langPractice.global.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.domain.user.repository.UserRepository;
import com.ndinhchien.langPractice.global.exception.ErrorMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            return new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND);
        });
        return new UserDetailsImpl(user);
    }

}