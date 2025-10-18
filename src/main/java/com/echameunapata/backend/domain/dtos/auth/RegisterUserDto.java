package com.echameunapata.backend.domain.dtos.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterUserDto {
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
