package com.example.week07_lab.service;

import com.example.week07_lab.dto.LoginRequestDTO;
import com.example.week07_lab.dto.TokenResponseDTO;
import com.example.week07_lab.exception.UnauthorizedException;
import com.example.week07_lab.model.User;
import com.example.week07_lab.repository.UserRepository;
import com.example.week07_lab.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Email no registrado"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new UnauthorizedException("Contraseña incorrecta");

        return new TokenResponseDTO(jwtService.generateToken(user));
    }
}
