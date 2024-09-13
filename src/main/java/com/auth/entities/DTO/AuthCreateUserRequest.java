package com.auth.entities.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(
		@NotBlank String email,
        @NotBlank String password,
        @NotBlank String name,
        @Valid AuthCreateRoleRequest roleRequest) {

}
