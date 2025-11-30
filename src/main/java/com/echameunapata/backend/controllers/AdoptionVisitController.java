package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.adoption.application.FindApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.application.visit.FindVisitDto;
import com.echameunapata.backend.domain.dtos.adoption.application.visit.ProgramingVisitDto;
import com.echameunapata.backend.domain.dtos.adoption.application.visit.UpdateVisitDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAdoptionVisitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base-path}/adoption/visit")
@AllArgsConstructor
public class AdoptionVisitController {

    private final IAdoptionVisitService adoptionVisitService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse>createVisit(@RequestBody @Valid ProgramingVisitDto visitDto){
        try{
            adoptionVisitService.programingVisit(visitDto);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success programing visit");
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<GeneralResponse>updateStatusVisit(@RequestBody @Valid UpdateVisitDto visitDto){
        try{
            var visit = adoptionVisitService.updateStatusAndObservations(visitDto);
            FindVisitDto resp = modelMapper.map(visit, FindVisitDto.class);

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }
}
