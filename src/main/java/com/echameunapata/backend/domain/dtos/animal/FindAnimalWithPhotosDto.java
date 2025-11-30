package com.echameunapata.backend.domain.dtos.animal;

import com.echameunapata.backend.domain.dtos.image.FindImagesDto;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class FindAnimalWithPhotosDto {
    private UUID id;
    private String name;
    private String species;

    private String sex;
    private String race;
    //despues pasar a edad
    private LocalDate birthDate;
    private AnimalState state;
    private LocalDate rescueDate;
    private String rescueLocation;
    private String initialDescription;
    private Boolean sterilized;
    private Boolean missingLimb = false;
    private List<FindImagesDto> photos;


}
