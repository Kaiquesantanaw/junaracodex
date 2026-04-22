package com.portfoliosass.service;

import com.portfoliosass.dto.RegisterRequest;
import com.portfoliosass.model.User;
import com.portfoliosass.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @NonNull
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username já está em uso.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já está em uso.");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.ROLE_USER)
                .enabled(true)
                .build();

        return Objects.requireNonNull(userRepository.save(user));
    }
}
