package com.echameunapata.backend.domain.dtos.adoption.application.visit;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionVisitStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class UpdateVisitDto {

    @NotNull
    private UUID id;
    @NotNull()
    private AdoptionVisitStatus status;
    private String observations;

}
