package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.reports.CreateReportDto;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/reports")
@AllArgsConstructor
public class ReportController {

    private final IReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse>createNewReport(@RequestBody @Valid CreateReportDto reportDto){
        try{
            Report res = reportService.createReport(reportDto);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", res);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

}
