package com.projekat.profesori_predmeti.service;

import com.projekat.profesori_predmeti.dto.*;
import com.projekat.profesori_predmeti.entity.User;
import com.projekat.profesori_predmeti.repository.UserRepository;
import com.projekat.profesori_predmeti.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException(
                    "Username '" + req.getUsername() + "' je vec zauzet");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException(
                    "Email '" + req.getEmail() + "' je vec registrovan");
        }

        User.Role role = User.Role.USER;
//        if ("ADMIN".equalsIgnoreCase(req.getRole())) {
//            role = User.Role.ADMIN;
//        }

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(
                user.getUsername(), user.getRole().name());

        return new AuthResponse(token, user.getUsername(),
                user.getRole().name(), "Registracija uspesna!");
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(), req.getPassword()));

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow();

        String token = jwtService.generateToken(
                user.getUsername(), user.getRole().name());

        return new AuthResponse(token, user.getUsername(),
                user.getRole().name(), "Login uspesan!");
    }
}