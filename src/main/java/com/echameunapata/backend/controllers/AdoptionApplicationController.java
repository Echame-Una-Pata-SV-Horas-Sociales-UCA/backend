package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.adoption.CreateApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.FindApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.FindApplicationWithPersonAndAnimalDto;
import com.echameunapata.backend.domain.dtos.adoption.UpdateStatusInApplicationDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.commons.PageResponse;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAdoptionApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

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
            FindApplicationWithPersonAndAnimalDto resp = modelMapper.map(application, FindApplicationWithPersonAndAnimalDto.class);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse>findAllApplications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            Pageable pageable
    ){
        try{
            Page<AdoptionApplication> applications = adoptionApplicationService.findAllApplications(status, startDate, endDate, pageable);
            Page<FindApplicationWithPersonAndAnimalDto> dtoPage = applications.map(application -> modelMapper.map(application, FindApplicationWithPersonAndAnimalDto.class));

            PageResponse<FindApplicationWithPersonAndAnimalDto> response = new PageResponse<>(
                    dtoPage.getContent(),
                    dtoPage.getNumber(),
                    dtoPage.getSize(),
                    dtoPage.getTotalElements(),
                    dtoPage.getTotalPages()
            );

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", response);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());}
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse>findApplicationById(@PathVariable("id")UUID id){
        try{
            AdoptionApplication application = adoptionApplicationService.findApplicationById(id);
            FindApplicationWithPersonAndAnimalDto applicationDto  = modelMapper.map(application, FindApplicationWithPersonAndAnimalDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", applicationDto);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @PutMapping("/update-application")
    public ResponseEntity<GeneralResponse>updateApplicationData(@RequestBody @Valid UpdateStatusInApplicationDto applicationDto){
        try{
            AdoptionApplication application = adoptionApplicationService.updateStatusAndDescription(applicationDto);
            FindApplicationDto res = modelMapper.map(application, FindApplicationDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", res);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

}
