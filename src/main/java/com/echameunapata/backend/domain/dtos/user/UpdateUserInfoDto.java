package com.echameunapata.backend.domain.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserInfoDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "Name cannot be email")
    private String email;
}
