package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.reportEvidence.CreateEvidenceDto;
import com.echameunapata.backend.domain.dtos.reportEvidence.FindEvidencesDto;
import com.echameunapata.backend.domain.models.ReportEvidence;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IEvidenceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.base-path}/evidence")
@AllArgsConstructor
public class EvidenceController {

    private final IEvidenceService evidenceService;
    private final ModelMapper modelMapper;

    @PostMapping(
            value = "/create/{reportId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<GeneralResponse> createEvidence(@ModelAttribute @Valid CreateEvidenceDto evidenceDto, @PathVariable("reportId")UUID reportId) throws IOException {
        try {
            evidenceService.createEvidence(evidenceDto, reportId);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "success");
        } catch (HttpError e) {
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());

        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse>deleteEvidence(@PathVariable("id") UUID id){
        try{
           evidenceService.deleteEvidence(id);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "success deleted evidence");
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/find-by-report/{reportId}")
    public ResponseEntity<GeneralResponse>findAllByReport(@PathVariable("reportId") UUID reportId){
        try{
            List<ReportEvidence> evidences = evidenceService.findAllEvidencesByReport(reportId);

            List<FindEvidencesDto> dtoList = evidences.stream()
                    .map(e -> modelMapper.map(e, FindEvidencesDto.class))
                    .collect(Collectors.toList());

            return GeneralResponse.getResponse(HttpStatus.OK, "success", dtoList);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<GeneralResponse>findById(@PathVariable("id") UUID id){
        try{
            ReportEvidence evidences = evidenceService.findEvidenceById(id);

            FindEvidencesDto resp = modelMapper.map(evidences, FindEvidencesDto.class);

            return GeneralResponse.getResponse(HttpStatus.OK, "success", resp);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

}
