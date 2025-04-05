package com.ndinhchien.langPractice.domain.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ndinhchien.langPractice.domain.user.dto.AuthRequestDto.LoginRequestDto;
import com.ndinhchien.langPractice.domain.user.dto.AuthRequestDto.RegisterRequestDto;
import com.ndinhchien.langPractice.domain.user.dto.AuthResponseDto.JwtResponseDto;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.domain.user.repository.UserRepository;
import com.ndinhchien.langPractice.global.auth.JwtService;
import com.ndinhchien.langPractice.global.exception.BusinessException;
import com.ndinhchien.langPractice.global.exception.ErrorMessage;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegisterRequestDto requestDto) {
        String password = requestDto.getPassword();
        String email = requestDto.getEmail();
        String userName = requestDto.getUserName();

        if (!password.equals(requestDto.getConfirmPassword())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.PASSWORD_MISMATCH);
        }
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.EMAIL_REGISTERED);
        }
        if (userRepository.existsByUserName(userName)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.USERNAME_EXISTED);
        }
        User user = User.builder()
                .userName(userName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        return userRepository.save(user);
    }

    public JwtResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = validateUser(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.INCORRECT_PASSWORD);
        }

        return jwtService.addTokenToResponse(user, response);

    }

    public void logout(User user, HttpServletResponse response) {
        jwtService.removeTokenFromResponse(user, response);
    }

    private User validateUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            return new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.USER_NOT_FOUND);
        });
    }
}