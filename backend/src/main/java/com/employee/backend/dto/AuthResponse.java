package com.employee.backend.dto;

public class AuthResponse {
    private String token;

    public AuthResponse() {}
    
    public AuthResponse(String token) {
        this.token = token;
    }

    // Manual Getter and Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
