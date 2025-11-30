package com.echameunapata.backend.domain.dtos.adoption.reference;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateReferenceDto {
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Phone Number cannot be empty")
    private String phoneNumber;
}
