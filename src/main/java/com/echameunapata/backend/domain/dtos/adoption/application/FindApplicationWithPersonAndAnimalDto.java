package com.echameunapata.backend.domain.dtos.adoption.application;

import com.echameunapata.backend.domain.dtos.adoption.application.visit.FindVisitDto;
import com.echameunapata.backend.domain.dtos.animal.FindAnimalDto;
import com.echameunapata.backend.domain.dtos.person.PersonDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindApplicationWithPersonAndAnimalDto extends FindApplicationDto {

    private PersonDto person;

    private FindAnimalDto animal;
    private List<FindVisitDto> adoptionVisits;
}
