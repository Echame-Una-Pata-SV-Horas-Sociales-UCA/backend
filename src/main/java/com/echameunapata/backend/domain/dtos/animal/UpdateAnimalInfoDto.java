package com.echameunapata.backend.domain.dtos.animal;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateAnimalInfoDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    private AnimalSex sex;
    private String race;

    private AnimalState state;
    private LocalDate rescueDate;
    private String rescueLocation;

    private Boolean sterilized = false;
    private Boolean missingLimb;

    private String observations;
}
