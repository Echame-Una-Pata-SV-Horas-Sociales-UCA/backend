package com.echameunapata.backend.domain.dtos.animal;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalSpecies;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
