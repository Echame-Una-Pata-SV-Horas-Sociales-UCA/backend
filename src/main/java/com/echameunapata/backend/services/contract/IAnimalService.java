package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;
import com.echameunapata.backend.domain.dtos.animal.UpdateAnimalInfoDto;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.models.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IAnimalService {
    Animal registerAnimal(RegisterAnimalDto animalDto) throws IOException;
    void updateAnimalInformation(UUID animalId, UpdateAnimalInfoDto animalInfoDto) throws IOException;
    List<Animal> findAllAnimals(String stateString, String sexString);
    Animal findById(UUID animalId);
    Animal findByName(String name);
    void updateAnimalStatus(UUID animalId, AnimalState status);
}
