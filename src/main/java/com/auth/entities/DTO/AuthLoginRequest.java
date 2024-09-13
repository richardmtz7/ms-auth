package com.auth.entities.DTO;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
		@NotBlank String email,
        @NotBlank String password) {

}
