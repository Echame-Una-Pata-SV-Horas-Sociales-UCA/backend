package com.echameunapata.backend.domain.dtos.animal;


import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalSpecies;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class RegisterAnimalDto {

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Specie cannot be empty")
    private AnimalSpecies species;

    private AnimalSex sex;
    private String race;
    private String age;

    private LocalDate rescueDate;
    @NotEmpty(message = "Rescue location  cannot be empty")
    private String rescueLocation;

    @Column(columnDefinition = "TEXT")
    private String initialDescription;

    private Boolean missingLimb;
    @Column(columnDefinition = "TEXT")
    private String observations;

    private MultipartFile photo;
}
