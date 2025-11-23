package com.echameunapata.backend.domain.dtos.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class PersonDto {

    private UUID id;
    private String firstNames;
    private String lastNames;
    private String email;
    private String phoneNumber;
    private String dui;
    private String address;
    private String city;
}
