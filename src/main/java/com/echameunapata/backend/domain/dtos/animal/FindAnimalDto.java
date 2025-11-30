package com.echameunapata.backend.domain.dtos.animal;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class FindAnimalDto {

    private UUID id;
    private String name;
    private String species;

    private String sex;
    private String race;
    private LocalDate birthDate;
}
