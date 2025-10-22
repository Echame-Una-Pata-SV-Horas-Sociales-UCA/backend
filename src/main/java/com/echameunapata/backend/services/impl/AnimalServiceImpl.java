package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;

import com.echameunapata.backend.domain.dtos.animal.UpdateAnimalInfoDto;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AnimalRepository;
import com.echameunapata.backend.services.contract.IAnimalService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AnimalServiceImpl implements IAnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;

    public AnimalServiceImpl(AnimalRepository animalRepository, ModelMapper modelMapper) {
        this.animalRepository = animalRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Animal registerAnimal(RegisterAnimalDto animalDto) {
        try{
            var animal = animalRepository.findByName(animalDto.getName());
            if(animal != null){
                throw new HttpError(HttpStatus.CONFLICT, "Name of the animal already in use");
            }

            animal = modelMapper.map(animalDto, Animal.class);

            return animalRepository.save(animal);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void updateAnimalInformation(UUID animalId, UpdateAnimalInfoDto animalInfoDto) {
        try{
            var animal = animalRepository.findById(animalId).orElse(null);
            if(animal == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "Thid animal not exists");
            }

            //Agregar el mapeo de los nuevo datos
        }catch (Exception e){
            throw e;
        }
    }

}
