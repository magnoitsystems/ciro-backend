package com.ciro.backend.dto;

import com.ciro.backend.enums.Role;

public class UserUpdateDTO {
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String color;
    private Role role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}