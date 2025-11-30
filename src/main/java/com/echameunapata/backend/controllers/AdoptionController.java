package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.adoption.FindAdoptionDto;
import com.echameunapata.backend.domain.dtos.adoption.application.FindApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.application.FindApplicationWithPersonAndAnimalDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.commons.PageResponse;
import com.echameunapata.backend.domain.models.Adoption;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAdoptionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("${api.base-path}/adoption")
@AllArgsConstructor
public class AdoptionController {

    private final IAdoptionService adoptionService;
    private final ModelMapper modelMapper;

    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse>findAllAdoptions(Pageable pageable){
        try{
            Page<Adoption> adoptions = adoptionService.findAllAdoptions(pageable);

            Page<FindAdoptionDto> dtoPage = adoptions.map(application -> modelMapper.map(application, FindAdoptionDto.class));

            PageResponse<FindAdoptionDto> response = new PageResponse<>(
                    dtoPage.getContent(),
                    dtoPage.getNumber(),
                    dtoPage.getSize(),
                    dtoPage.getTotalElements(),
                    dtoPage.getTotalPages()
            );

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", response);

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
