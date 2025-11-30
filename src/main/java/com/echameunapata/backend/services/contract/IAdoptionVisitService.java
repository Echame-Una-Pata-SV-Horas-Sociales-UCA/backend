package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.adoption.application.visit.ProgramingVisitDto;
import com.echameunapata.backend.domain.dtos.adoption.application.visit.UpdateVisitDto;
import com.echameunapata.backend.domain.models.AdoptionVisit;

public interface IAdoptionVisitService {
    void programingVisit(ProgramingVisitDto visitDto);
    AdoptionVisit updateStatusAndObservations(UpdateVisitDto updateVisitDto);
}
