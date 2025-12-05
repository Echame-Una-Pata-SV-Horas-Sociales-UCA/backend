package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.adoption.FindAdoptionDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.models.Adoption;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAdoptionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.base-path}/adoption")
@AllArgsConstructor
public class AdoptionController {

    private final IAdoptionService adoptionService;
    private final ModelMapper modelMapper;

    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse>findAllAdoptions(){
        try{
            List<Adoption> adoptions = adoptionService.findAllAdoptions();

            List<FindAdoptionDto> dtoPage = adoptions.stream().map(application -> modelMapper.map(application, FindAdoptionDto.class)).toList();


            return GeneralResponse.getResponse(HttpStatus.OK, "Success", dtoPage);

        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse>findById(@PathVariable("id")UUID id){
        try{
            Adoption adoption = adoptionService.findAdoptionById(id);
            FindAdoptionDto resp = modelMapper.map(adoption, FindAdoptionDto.class);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }
}
