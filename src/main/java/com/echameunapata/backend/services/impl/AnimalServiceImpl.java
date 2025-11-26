package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;

import com.echameunapata.backend.domain.dtos.animal.UpdateAnimalInfoDto;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AnimalRepository;
import com.echameunapata.backend.services.contract.IAnimalService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AnimalServiceImpl implements IAnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;

    public AnimalServiceImpl(AnimalRepository animalRepository, ModelMapper modelMapper) {
        this.animalRepository = animalRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Registra un nuevo animal en el sistema.
     *
     * Este método valida que no exista otro animal con el mismo nombre.
     * Si el nombre ya está en uso, se lanza un error de conflicto.
     *
     * @param animalDto información necesaria para registrar un animal.
     * @return El animal registrado.
     * @throws HttpError Si el nombre ya está en uso u ocurre un error inesperado.
     */
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

    /**
     * Actualiza la información de un animal existente.
     *
     * Este método busca el animal por su ID, valida su existencia
     * y actualiza únicamente la información enviada desde el DTO.
     *
     * @param animalId identificador único del animal a actualizar.
     * @param animalInfoDto información nueva del animal.
     * @throws HttpError Si el animal no existe o ocurre un error inesperado.
     */
    @Override
    public void updateAnimalInformation(UUID animalId, UpdateAnimalInfoDto animalInfoDto) {
        try{
            var animal = animalRepository.findById(animalId).orElse(null);
            if(animal == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "This animal not exists");
            }

            modelMapper.map(animalInfoDto, animal);
            animalRepository.save(animal);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Obtiene una lista paginada de animales filtrados por su estado.
     *
     * Si el estado no se especifica, se devuelven todos los animales.
     * Si se envía un estado válido, se filtra por dicho estado.
     *
     * @param stateString estado del animal en texto (opcional).
     * @param pageable información de paginación.
     * @return Página con la lista de animales encontrados.
     * @throws HttpError Si ocurre un error inesperado durante la consulta.
     */
    @Override
    public Page<Animal> findAllAnimalsState(String stateString, Pageable pageable) {
        try{
            Page<Animal> animals;

            if (stateString.isEmpty()){
               animals = animalRepository.findAll(pageable);
            }
            else{
                AnimalState state = AnimalState.fromString(stateString);
                animals = animalRepository.findAllByState(state, pageable);
            }

            return animals;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Busca y obtiene un animal por su identificador único.
     *
     * @param animalId identificador único del animal.
     * @return El animal encontrado.
     * @throws HttpError Si no existe un animal con ese ID o ocurre un error inesperado.
     */
    @Override
    public Animal findById(UUID animalId) {
        try{
            var animal = animalRepository.findById(animalId).orElse(null);

            if(animal==null){
                throw new HttpError(HttpStatus.NOT_FOUND, "This animal not exists");
            }

            return animal;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Busca y obtiene un animal por su nombre.
     *
     * @param name nombre del animal a buscar.
     * @return El animal encontrado.
     * @throws HttpError Si no existe un animal con ese nombre o ocurre un error inesperado.
     */
    @Override
    public Animal findByName(String name) {
        try{
            var animal = animalRepository.findByName(name);
            if(animal == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "The animal with that name does not exist");
            }

            return animal;
        }catch (Exception e){
            throw e;
        }
    }

}
