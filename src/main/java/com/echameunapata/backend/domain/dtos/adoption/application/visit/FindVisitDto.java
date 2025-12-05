package com.echameunapata.backend.domain.dtos.adoption.application.visit;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionVisitStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class FindVisitDto {

    private UUID id;

    private String scheduledDate;
    private AdoptionVisitStatus status = AdoptionVisitStatus.SCHEDULED;
    private String observations;
    private String evaluatorName;
    private Timestamp createAt;
}
