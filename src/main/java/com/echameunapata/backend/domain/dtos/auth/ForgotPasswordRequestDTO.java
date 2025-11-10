package com.echameunapata.backend.domain.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email format is invalid")
    private String email;
}
