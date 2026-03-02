package com.ciro.backend.dto;

public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String name;

    public AuthResponseDTO(String accessToken, String refreshToken, Long userId, String name) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.name = name;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
}