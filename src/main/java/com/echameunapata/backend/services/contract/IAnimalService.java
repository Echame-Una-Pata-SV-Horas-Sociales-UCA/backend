package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;
import com.echameunapata.backend.domain.dtos.animal.UpdateAnimalInfoDto;
import com.echameunapata.backend.domain.models.Animal;

import java.util.UUID;

public interface IAnimalService {
    Animal registerAnimal(RegisterAnimalDto animalDto);
    void updateAnimalInformation(UUID animalId, UpdateAnimalInfoDto animalInfoDto);
}
