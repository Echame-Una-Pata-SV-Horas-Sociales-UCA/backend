package com.echameunapata.backend.domain.dtos.adoption.application;

import com.echameunapata.backend.domain.dtos.adoption.reference.CreateReferenceDto;
import com.echameunapata.backend.domain.dtos.person.CreatePersonDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateApplicationDto {

    private String status;
    private Boolean ownHome;
    private Boolean acceptsVisits;
    private String veterinarianName;
    private String veterinarianPhone;
    private Boolean commitmentToSterilization;
    private Boolean commitmentToSendPhotos;
    private String description;
    private CreatePersonDto person;
    private UUID animalId;
    private List<CreateReferenceDto>references;
}
