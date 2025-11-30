package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;
import com.echameunapata.backend.domain.dtos.animal.UpdateAnimalInfoDto;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.models.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface IAnimalService {
    Animal registerAnimal(RegisterAnimalDto animalDto);
    void updateAnimalInformation(UUID animalId, UpdateAnimalInfoDto animalInfoDto);
    Page<Animal> findAllAnimalsState(String  stateString, Pageable pageable);
    Animal findById(UUID animalId);
    Animal findByName(String name);
    Animal updateAnimalStatus(UUID animalId, AnimalState status);
}
