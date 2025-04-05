package com.ndinhchien.langPractice.global.util;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateSecureString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static List<Long> parseToLongList(String data) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            Long[] array = mapper.readValue(data, Long[].class);
            return Arrays.stream(array).toList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static Instant parseInstant(String timestamp) {
        return (timestamp != null && !timestamp.isBlank()) ? Instant.parse(timestamp) : null;
    }
}
