package com.echameunapata.backend.domain.dtos.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdatePersonInfoDto {

    @NotEmpty
    @Email(message = "Invalid format by email")
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
}
