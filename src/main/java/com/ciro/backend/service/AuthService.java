package com.ciro.backend.service;

import com.ciro.backend.dto.AuthResponseDTO;
import com.ciro.backend.dto.LoginRequestDTO;
import com.ciro.backend.entity.User;
import com.ciro.backend.exception.BadRequestException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getHashedPassword())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponseDTO(accessToken, refreshToken, user.getId(), user.getName());
    }

    public AuthResponseDTO refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            if (jwtService.isTokenValid(refreshToken, user.getUsername())) {
                String newAccessToken = jwtService.generateAccessToken(user);

                return new AuthResponseDTO(newAccessToken, refreshToken, user.getId(), user.getName());
            }
        }
        throw new BadRequestException("Refresh token inválido o expirado");
    }
}