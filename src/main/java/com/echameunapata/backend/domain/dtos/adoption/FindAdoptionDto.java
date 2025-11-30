package com.echameunapata.backend.domain.dtos.adoption;

import com.echameunapata.backend.domain.dtos.adoption.application.FindApplicationDto;
import com.echameunapata.backend.domain.dtos.animal.FindAnimalDto;
import com.echameunapata.backend.domain.dtos.person.PersonDto;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
public class FindAdoptionDto {

    private UUID id;
    private Instant deliveryDate;
    private Boolean active_monitoring;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private FindAnimalDto animal;
    private PersonDto adopter;

    private FindApplicationDto adoptionApplication;
}
