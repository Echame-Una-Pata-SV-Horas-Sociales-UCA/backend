package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.animal.FindAnimalDto;
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
}
