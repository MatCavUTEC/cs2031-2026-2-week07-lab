package com.example.week07_lab.service;

import com.example.week07_lab.dto.RegisterRequestDTO;
import com.example.week07_lab.dto.RegisterResponseDTO;
import com.example.week07_lab.exception.ConflictException;
import com.example.week07_lab.model.User;
import com.example.week07_lab.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new ConflictException("El email ya está registrado");

        // Se arma a mano (no ModelMapper) porque la contraseña debe guardarse encriptada
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return new RegisterResponseDTO(userRepository.save(user).getId());
    }
}
