package com.echameunapata.backend.domain.dtos.adoption.reference;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateReferenceDto {
    private String name;
    private String phoneNumber;
}
