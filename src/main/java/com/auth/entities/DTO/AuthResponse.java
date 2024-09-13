package com.auth.entities.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "message", "status", "jwt"})
public record AuthResponse(
		String email,
        String message,
        Boolean status,
        String jwt){
}
