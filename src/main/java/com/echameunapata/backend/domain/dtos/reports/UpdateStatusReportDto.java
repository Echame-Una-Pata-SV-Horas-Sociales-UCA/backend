package com.echameunapata.backend.domain.dtos.reports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatusReportDto {
    @NotBlank(message = "The 'status' field cannot be empty")
    private String status;
    @NotNull(message = "The 'orderId' field cannot be empty")
    private UUID reportId;
}
