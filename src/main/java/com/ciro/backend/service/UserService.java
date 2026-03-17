package com.ciro.backend.service;

import com.ciro.backend.dto.UserCreateDTO;
import com.ciro.backend.dto.UserResponseDTO;
import com.ciro.backend.entity.User;
import com.ciro.backend.enums.Role;
import com.ciro.backend.exception.DuplicateResourceException;
import com.ciro.backend.exception.ResourceNotFoundException;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("El username "+ dto.getUsername()+" ya está en uso");
        }

        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setLastname(dto.getLastname());
        newUser.setUsername(dto.getUsername());

        String userColor = (dto.getColor() != null && !dto.getColor().trim().isEmpty())
                ? dto.getColor()
                : "#9E9E9E";
        newUser.setColor(userColor);

        String hashedPassword = passwordService.hashPassword(dto.getPassword());
        newUser.setHashedPassword(hashedPassword);
        newUser.setRole(Role.USER);

        User savedUser = userRepository.save(newUser);
        return mapToResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró el usuario con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setColor(user.getColor());
        dto.setRole(user.getRole());
        return dto;
    }

    @Transactional
    public UserResponseDTO toggleUserRole(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con ID: " + id));

        if (user.getRole() == Role.ADMIN) {
            user.setRole(Role.USER);
        } else {
            user.setRole(Role.ADMIN);
        }

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }
}