package com.ciro.backend.controller;

import com.ciro.backend.dto.UserCreateDTO;
import com.ciro.backend.dto.UserResponseDTO;
import com.ciro.backend.entity.User;
import com.ciro.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO userDTO) {
        UserResponseDTO newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> toggleUserRole(@PathVariable Long id) {
        UserResponseDTO updatedUser = userService.toggleUserRole(id);
        return ResponseEntity.ok(updatedUser);
    }
}