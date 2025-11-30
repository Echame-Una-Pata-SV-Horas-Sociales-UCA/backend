package com.echameunapata.backend.domain.dtos.adoption.application.visit;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ProgramingVisitDto {

    @NotNull
    private String scheduledDate;

    private String evaluatorName;
    private UUID applicationId;
}
