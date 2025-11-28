package com.echameunapata.backend.domain.dtos.adoption;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
public class FindApplicationDto {
    private UUID id;

    private Instant applicationDate;
    private String status;
    private Boolean ownHome;
    private Boolean acceptsVisits;
    private String veterinarianName;
    private String veterinarianPhone;
    private Boolean commitmentToSterilization;
    private Boolean commitmentToSendPhotos;

    private String observations;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
