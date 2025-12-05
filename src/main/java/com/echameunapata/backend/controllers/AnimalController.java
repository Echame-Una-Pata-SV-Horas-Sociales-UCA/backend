package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.animal.FindAnimalDto;
import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;
import com.echameunapata.backend.domain.dtos.animal.UpdateAnimalInfoDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAnimalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-path}/animal")
@AllArgsConstructor
public class AnimalController {

    private final IAnimalService animalService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse>registerAnimal(@ModelAttribute @Valid RegisterAnimalDto animalDto){
        try{
            Animal animal = animalService.registerAnimal(animalDto);
            FindAnimalDto resp = modelMapper.map(animal, FindAnimalDto.class);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateAnimal(
            @PathVariable("id") UUID id,
            @ModelAttribute UpdateAnimalInfoDto updateDto){
        try{
            // Validate that at least one field is provided
            if (isUpdateDtoEmpty(updateDto)) {
                return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "No fields provided to update");
            }

            animalService.updateAnimalInformation(id, updateDto);
            Animal updatedAnimal = animalService.findById(id);
            FindAnimalDto resp = modelMapper.map(updatedAnimal, FindAnimalDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "Animal updated successfully", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica si el DTO de actualización está vacío (sin campos para actualizar).
     *
     * @param dto DTO con la información a actualizar
     * @return true si todos los campos son null o vacíos, false si hay al menos un campo
     */
    private boolean isUpdateDtoEmpty(UpdateAnimalInfoDto dto) {
        return dto.getName() == null &&
               dto.getSpecies() == null &&
               dto.getSex() == null &&
               dto.getRace() == null &&
               dto.getAge() == null &&
               dto.getRescueDate() == null &&
               dto.getRescueLocation() == null &&
               dto.getInitialDescription() == null &&
               dto.getSterilized() == null &&
               dto.getMissingLimb() == null &&
               dto.getObservations() == null &&
               (dto.getPhoto() == null || dto.getPhoto().isEmpty());
    }

    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse> findAllAnimals(
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String state){
        try{
            List<Animal> animals = animalService.findAllAnimals(state, sex);
            List<FindAnimalDto> dtoPage = animals.stream().map(application -> modelMapper.map(application, FindAnimalDto.class)).toList();


            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", dtoPage);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse>findAnimalById(@PathVariable("id")UUID id){
        try{
            Animal animal = animalService.findById(id);
            FindAnimalDto resp = modelMapper.map(animal, FindAnimalDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "Success", resp);
        }catch (HttpError e){
            throw e;
        }
    }
}
