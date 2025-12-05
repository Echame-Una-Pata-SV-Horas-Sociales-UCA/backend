package com.echameunapata.backend.domain.dtos.adoption.application;

import com.echameunapata.backend.domain.dtos.adoption.application.visit.FindVisitDto;
import com.echameunapata.backend.domain.dtos.adoption.reference.CreateReferenceDto;
import com.echameunapata.backend.domain.dtos.animal.FindAnimalDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindApplicationWithPersonAndAnimalDto extends FindApplicationDto {

    private List<CreateReferenceDto> adoptionReferences;
    private FindAnimalDto animal;
    private List<FindVisitDto> adoptionVisits;
}
