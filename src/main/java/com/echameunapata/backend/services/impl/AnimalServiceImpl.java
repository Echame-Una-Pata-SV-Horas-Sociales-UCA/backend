package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;

import com.echameunapata.backend.domain.dtos.animal.UpdateAnimalInfoDto;
import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.AnimalPhoto;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AnimalPhotoRepository;
import com.echameunapata.backend.repositories.AnimalRepository;
import com.echameunapata.backend.services.contract.IAnimalService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AnimalServiceImpl implements IAnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    private final AnimalPhotoRepository animalPhotoRepository;
    private final FileStorageServiceImpl fileStorageService;

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
    public Animal registerAnimal(RegisterAnimalDto animalDto) throws IOException {
        try{

            if(animalRepository.findByName(animalDto.getName()) != null){
                throw new HttpError(HttpStatus.CONFLICT, "Name of the animal already in use");
            }

            Animal animal = getAnimal(animalDto);

            String photo = fileStorageService.uploadFile(animalDto.getPhoto(), "animals/" + animal.getName());
            animal.setPhoto(photo);

            return animalRepository.save(animal);
        }catch (HttpError e){
            throw e;
        }
    }

    private static Animal getAnimal(RegisterAnimalDto animalDto) {
        Animal animal = new Animal();
        animal.setName(animalDto.getName());
        animal.setSpecies(animalDto.getSpecies());
        animal.setSex(animalDto.getSex());
        animal.setRace(animalDto.getRace());
        animal.setAge(animalDto.getAge());
        animal.setRescueDate(animalDto.getRescueDate());
        animal.setRescueLocation(animalDto.getRescueLocation());
        animal.setInitialDescription(animalDto.getInitialDescription());
        animal.setObservations(animalDto.getObservations());

        if (animalDto.getMissingLimb() == null) {
            animal.setMissingLimb(false);
        } else {
            animal.setMissingLimb(animalDto.getMissingLimb());
        }

        animal.setState(animalDto.getState() != null ? animalDto.getState() : AnimalState.AVAILABLE);

        if (animalDto.getSterilized() != null) {
            animal.setSterilized(animalDto.getSterilized());
        } else {
            animal.setSterilized(false);
        }

        return animal;
    }


    /**
     * Actualiza la información de un animal existente.
     *
     * Este método busca el animal por su ID, valida su existencia
     * y actualiza únicamente la información enviada desde el DTO.
     * Si se proporciona una nueva foto, elimina la anterior del servidor
     * y sube la nueva.
     *
     * @param animalId identificador único del animal a actualizar.
     * @param animalInfoDto información nueva del animal.
     * @throws HttpError Si el animal no existe o ocurre un error inesperado.
     * @throws IOException Si ocurre un error al subir o eliminar la foto.
     */
    @Override
    public void updateAnimalInformation(UUID animalId, UpdateAnimalInfoDto animalInfoDto) throws IOException {
        try{
            var animal = animalRepository.findById(animalId).orElse(null);
            if(animal == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "This animal not exists");
            }

            boolean changed = false;

            // Update fields only if they are provided (not null)
            if(animalInfoDto.getName() != null){
                // Check if name is already in use by another animal
                Animal existingAnimal = animalRepository.findByName(animalInfoDto.getName());
                if(existingAnimal != null && !existingAnimal.getId().equals(animalId)){
                    throw new HttpError(HttpStatus.CONFLICT, "Name of the animal already in use");
                }
                animal.setName(animalInfoDto.getName());
                changed = true;
            }
            if(animalInfoDto.getSpecies() != null){
                animal.setSpecies(animalInfoDto.getSpecies());
                changed = true;
            }
            if(animalInfoDto.getSex() != null){
                // AnimalSex validation
                boolean validSex = false;
                for (AnimalSex s : AnimalSex.values()) {
                    if (s == animalInfoDto.getSex()) {
                        validSex = true;
                        break;
                    }
                }
                if (!validSex) {
                    throw new HttpError(HttpStatus.BAD_REQUEST, "Invalid animal sex: " + animalInfoDto.getSex());
                }
                if(animalInfoDto.getSex() != animal.getSex()){
                    animal.setSex(animalInfoDto.getSex());
                    changed = true;
                }
            }
            if(animalInfoDto.getRace() != null){
                animal.setRace(animalInfoDto.getRace());
                changed = true;
            }
            if(animalInfoDto.getAge() != null){
                // Age validation
                try {
                    int ageInt = Integer.parseInt(animalInfoDto.getAge().trim());
                    if (ageInt < 0) {
                        throw new HttpError(HttpStatus.BAD_REQUEST, "Animal age cannot be negative: " + animalInfoDto.getAge());
                    }
                } catch (NumberFormatException e) {
                    throw new HttpError(HttpStatus.BAD_REQUEST, "Animal age must be a valid integer: " + animalInfoDto.getAge());
                }
                if(!animalInfoDto.getAge().equals(animal.getAge())){
                    animal.setAge(animalInfoDto.getAge());
                    changed = true;
                }
            }
            if(animalInfoDto.getRescueDate() != null){
                animal.setRescueDate(animalInfoDto.getRescueDate());
                changed = true;
            }
            if(animalInfoDto.getRescueLocation() != null){
                animal.setRescueLocation(animalInfoDto.getRescueLocation());
                changed = true;
            }
            if(animalInfoDto.getInitialDescription() != null){
                animal.setInitialDescription(animalInfoDto.getInitialDescription());
                changed = true;
            }
            if(animalInfoDto.getSterilized() != null){
                animal.setSterilized(animalInfoDto.getSterilized());
                changed = true;
            }
            if(animalInfoDto.getMissingLimb() != null){
                animal.setMissingLimb(animalInfoDto.getMissingLimb());
                changed = true;
            }
            if(animalInfoDto.getObservations() != null){
                animal.setObservations(animalInfoDto.getObservations());
                changed = true;
            }
            if(animalInfoDto.getState() != null) {
                boolean validState = false;
                for (AnimalState s : AnimalState.values()) {
                    if (s == animalInfoDto.getState()) {
                        validState = true;
                        break;
                    }
                }
                if (!validState) {
                    throw new HttpError(HttpStatus.BAD_REQUEST, "Invalid animal state: " + animalInfoDto.getState());
                }
                if(animalInfoDto.getState() != animal.getState()){
                    animal.setState(animalInfoDto.getState());
                    changed = true;
                }
            }

            // Handle photo update: delete old photo and upload new one
            if(animalInfoDto.getPhoto() != null && !animalInfoDto.getPhoto().isEmpty()){
                // Delete old photo if exists
                if(animal.getPhoto() != null && !animal.getPhoto().isEmpty()){
                    // Extract public_id from the URL
                    String publicId = extractPublicIdFromUrl(animal.getPhoto());
                    if(publicId != null){
                        fileStorageService.deleteFile(publicId);
                    }
                }

                // Upload new photo
                String newPhotoUrl = fileStorageService.uploadFile(
                    animalInfoDto.getPhoto(),
                    "animals/" + animal.getName()
                );
                animal.setPhoto(newPhotoUrl);
                changed = true;
            }

            if(changed){
                animalRepository.save(animal);
            }
        }catch (HttpError e){
            throw e;
        }catch (IOException e){
            throw e;
        }
    }

    /**
     * Robustly extracts the Cloudinary public_id from a public URL.
     * Handles versioning, transformations, file extension, and query parameters.
     *
     * Examples:
     * https://res.cloudinary.com/demo/image/upload/v1234567890/echameunapata/animals/firulais.jpg
     *   -> echameunapata/animals/firulais
     * https://res.cloudinary.com/demo/image/upload/w_100,h_100,c_thumb/v1234567890/echameunapata/animals/firulais.jpg?_a=ABC
     *   -> echameunapata/animals/firulais
     */
    private String extractPublicIdFromUrl(String url) {
        if (url == null) return null;
        int uploadIdx = url.indexOf("/upload/");
        if (uploadIdx == -1) return null;
        String afterUpload = url.substring(uploadIdx + 8); // after '/upload/'

        // Remove query parameters
        int queryIdx = afterUpload.indexOf('?');
        if (queryIdx != -1) {
            afterUpload = afterUpload.substring(0, queryIdx);
        }

        // Remove transformations (anything before first folder or version)
        // Find first slash after possible transformations
        int firstFolderIdx = afterUpload.indexOf('/');
        if (firstFolderIdx != -1 && firstFolderIdx < 15) { // transformations are usually short
            afterUpload = afterUpload.substring(firstFolderIdx + 1);
        }

        // Remove version (e.g., v1234567890/)
        if (afterUpload.startsWith("v") && afterUpload.length() > 2 && Character.isDigit(afterUpload.charAt(1))) {
            int slashIdx = afterUpload.indexOf('/');
            if (slashIdx != -1) {
                afterUpload = afterUpload.substring(slashIdx + 1);
            }
        }

        // Remove file extension
        int dotIdx = afterUpload.lastIndexOf('.');
        if (dotIdx != -1) {
            afterUpload = afterUpload.substring(0, dotIdx);
        }

        return afterUpload;
    }

    /**
     * Obtiene una lista paginada de animales filtrados por su estado.
     *
     * Si el estado no se especifica, se devuelven todos los animales.
     * Si se envía un estado válido, se filtra por dicho estado.
     *
     * @param stateString estado del animal en texto (opcional).
     * @return Página con la lista de animales encontrados.
     * @throws HttpError Si ocurre un error inesperado durante la consulta.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Animal> findAllAnimals(String stateString, String sexString) {
        try{

            AnimalSex animalSex;

            // By default, when no value of sex is provided, Animal Sex will be UNKNOWN, but we want to search all when no sex is received here, so we set it to null
            if(sexString == null || (sexString != null && sexString.isBlank())){
                animalSex = null;
            }else{
                animalSex = AnimalSex.fromString(sexString);
            }

            AnimalState animalState = (stateString != null && !stateString.isBlank()) ? AnimalState.fromString(stateString) : null;

            return animalRepository.findAllByFilters(animalSex, animalState);
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

    @Override
    public void updateAnimalStatus(UUID animalId, AnimalState status) {
        try{
            var animal = animalRepository.findById(animalId).orElse(null);
            if(animal == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "The animal with that name does not exist");
            }

            animal.setState(status);
            animalRepository.save(animal);
        }catch (HttpError e){
            throw e;
        }
    }

}
