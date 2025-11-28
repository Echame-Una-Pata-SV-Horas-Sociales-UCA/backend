package com.echameunapata.backend.domain.dtos.adoption;

import com.echameunapata.backend.domain.dtos.person.CreatePersonDto;
import lombok.Data;

import java.time.LocalDate;
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


    private CreatePersonDto person;

    private UUID animalId;
}
