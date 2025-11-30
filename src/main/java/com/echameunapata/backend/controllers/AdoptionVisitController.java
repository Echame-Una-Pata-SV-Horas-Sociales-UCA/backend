package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.adoption.application.visit.ProgramingVisitDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAdoptionVisitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/adoption/visit")
@AllArgsConstructor
public class AdoptionVisitController {

    private final IAdoptionVisitService adoptionVisitService;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse>createVisit(@RequestBody @Valid ProgramingVisitDto visitDto){
        try{
            adoptionVisitService.programingVisit(visitDto);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success programing visit");
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }
}
