package com.echameunapata.backend.domain.dtos.reports;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatusReportDto {
    @NotBlank(message = "The 'status' field cannot be empty")
    private String status;
    @NotNull(message = "The 'reportId' field cannot be empty")
    private UUID reportId;
}
