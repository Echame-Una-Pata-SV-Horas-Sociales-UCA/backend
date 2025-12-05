package com.echameunapata.backend.domain.dtos.person;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class CreatePersonDto {

    @NotEmpty(message = "First names cannot be empty")
    private String firstNames;
    @NotEmpty(message = "Last names cannot be empty")
    private String lastNames;
    @NotEmpty
    @Email(message = "Invalid format by email")
    private String email;
    private String phoneNumber;
    @NotEmpty(message = "DUI cannot be empty")
    @Pattern(regexp = "\\d{8}-\\d", message = "DUI must be in the format ########-#")
    private String dui;
    private String address;
    private String city;

}
