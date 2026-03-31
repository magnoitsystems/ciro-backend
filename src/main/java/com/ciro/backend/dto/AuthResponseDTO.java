package com.ciro.backend.dto;

import com.ciro.backend.enums.Role;

public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String name;
    private String color;
    private Role role;

    public AuthResponseDTO(String accessToken, String refreshToken, Long userId, String name, String color, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.name = name;
        this.color = color;
        this.role = role;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public Role getRole() { return role; } // Getter del rol
}