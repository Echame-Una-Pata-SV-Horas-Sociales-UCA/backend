package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.animal.FindAnimalDto;
import com.echameunapata.backend.domain.dtos.animal.FindAnimalWithPhotosDto;
import com.echameunapata.backend.domain.dtos.animal.RegisterAnimalDto;
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
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse>findAllApplications(
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String state){
        try{
            List<Animal> animals = animalService.findAllAnimals(state, sex);
            List<FindAnimalWithPhotosDto> dtoPage = animals.stream().map(application -> modelMapper.map(application, FindAnimalWithPhotosDto.class)).toList();


            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", dtoPage);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse>findAnimalById(@PathVariable("id")UUID id){
        try{
            Animal animal = animalService.findById(id);
            FindAnimalWithPhotosDto resp = modelMapper.map(animal, FindAnimalWithPhotosDto.class);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", resp);
        }catch (HttpError e){
            throw e;
        }
    }
}
