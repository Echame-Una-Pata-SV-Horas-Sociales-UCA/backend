package com.echameunapata.backend.domain.dtos.animal;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateAnimalInfoDto {

    private String name;

    private AnimalSex sex;
    private String race;

    private AnimalState state;
    private LocalDate rescueDate;
    private String rescueLocation;

    private Boolean sterilized;
    private Boolean missingLimb;

    private String observations;

}
