package com.echameunapata.backend.domain.dtos.animal;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalSpecies;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UpdateAnimalInfoDto {

    private String name;
    private AnimalSpecies species;
    private AnimalSex sex;
    private String race;

    private AnimalState state;
    private String age;
    private LocalDate rescueDate;
    private String rescueLocation;
    private String initialDescription;
    private Boolean sterilized;
    private Boolean missingLimb;
    private String observations;
    private MultipartFile photo;
}
