package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.domain.dtos.reports.CreateReportDto;
import com.echameunapata.backend.domain.dtos.reports.FindReportDto;
import com.echameunapata.backend.domain.models.Report;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IReportService;
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

@RestController
@RequestMapping("${api.base-path}/reports")
@AllArgsConstructor
public class ReportController {

    private final IReportService reportService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse>createNewReport(@RequestBody @Valid CreateReportDto reportDto){
        try{
            Report res = reportService.createReport(reportDto);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success", res);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<GeneralResponse>findAllReports(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            Pageable pageable
    ){
        try {
            Page<Report> reports = reportService.findAllReportsByFilters(type, status, startDate, endDate, pageable);
            Page<FindReportDto> reportsDto = reports.map(report -> modelMapper.map(report, FindReportDto.class));

            return GeneralResponse.getResponse(HttpStatus.OK, "Success all reports", reportsDto);
        }catch (HttpError e){
            return GeneralResponse.getResponse(e.getStatus(), e.getMessage());
        }

    }

}
