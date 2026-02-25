package com.ciro.backend.service;

import com.ciro.backend.dto.UserCreateDTO;
import com.ciro.backend.entity.User;
import com.ciro.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Transactional
    public User createUser(UserCreateDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Error: El nombre de usuario '" + dto.getUsername() + "' ya está en uso.");
        }

        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setLastname(dto.getLastname());
        newUser.setUsername(dto.getUsername());

        String hashedPassword = passwordService.hashPassword(dto.getPassword());
        newUser.setHashedPassword(hashedPassword);

        return userRepository.save(newUser);
    }
}