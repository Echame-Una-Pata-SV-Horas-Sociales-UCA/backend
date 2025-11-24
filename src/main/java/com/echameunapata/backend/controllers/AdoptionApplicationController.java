package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.adoption.CreateApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.FindApplicationDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAdoptionApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/adoption")
@AllArgsConstructor
public class AdoptionApplicationController {

    private final IAdoptionApplicationService adoptionApplicationService;
    private final ModelMapper modelMapper;

    @PostMapping("/application")
    public ResponseEntity<GeneralResponse>createNewApplication(@RequestBody @Valid CreateApplicationDto applicationDto){
        try{
            AdoptionApplication application = adoptionApplicationService.createApplication(applicationDto);
            FindApplicationDto resp = modelMapper.map(application, FindApplicationDto.class);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }
}
