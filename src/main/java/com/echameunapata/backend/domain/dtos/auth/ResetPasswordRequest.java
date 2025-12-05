package com.echameunapata.backend.domain.dtos.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    @NotEmpty(message = "Reset token is required")
    private String token;

    @NotEmpty(message = "New password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    @NotEmpty(message = "Confirm password cannot be empty")
    private String confirmPassword;
}
