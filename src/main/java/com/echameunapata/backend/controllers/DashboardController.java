package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.services.contract.IDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("${api.base-path}/dashboards")
@AllArgsConstructor
public class DashboardController {
    private final IDashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<GeneralResponse> getMetrics() {
        try{
            Map<String, Object>metrics =dashboardService.getMetrics();

            return GeneralResponse.getResponse(HttpStatus.CREATED, "Success programing visit", metrics);
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error in get dashboards"+e.getMessage());
        }
    }
}
