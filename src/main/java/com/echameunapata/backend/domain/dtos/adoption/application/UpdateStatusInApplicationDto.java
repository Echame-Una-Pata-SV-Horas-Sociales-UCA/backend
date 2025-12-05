package com.echameunapata.backend.domain.dtos.adoption.application;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatusInApplicationDto {
    private UUID id;
    private String status;
    private String observations;
}
