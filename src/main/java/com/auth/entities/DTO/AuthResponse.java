package com.auth.entities.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"userId","email", "message", "role", "status", "jwt"})
public record AuthResponse(
		Integer userId,
		String email,
        String message,
        String role,
        Boolean status,
        String jwt){
}
