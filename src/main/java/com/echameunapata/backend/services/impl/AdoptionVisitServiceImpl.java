package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.adoption.application.visit.ProgramingVisitDto;
import com.echameunapata.backend.domain.dtos.adoption.application.visit.UpdateVisitDto;
import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.AdoptionVisit;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AdoptionVisitRepository;
import com.echameunapata.backend.services.contract.IAdoptionApplicationService;
import com.echameunapata.backend.services.contract.IAdoptionVisitService;
import com.echameunapata.backend.services.notifications.factory.NotificationFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdoptionVisitServiceImpl implements IAdoptionVisitService {

    private final AdoptionVisitRepository adoptionVisitRepository;
    private final IAdoptionApplicationService applicationService;
    private final NotificationFactory notificationFactory;

    @Override
    public void programingVisit(ProgramingVisitDto visitDto) {
        try{
            var application = applicationService.findApplicationById(visitDto.getApplicationId());
            var visit = adoptionVisitRepository.findByAdoptionApplication(application).orElse(null);

            if(visit != null){
                throw new HttpError(HttpStatus.CONFLICT, "Visit already programing");
            }

            visit = new AdoptionVisit();
            visit.setEvaluatorName(visitDto.getEvaluatorName());
            visit.setScheduledDate(visitDto.getScheduledDate());
            visit.setAdoptionApplication(application);

             visit = adoptionVisitRepository.save(visit);

            notificationFactory.getStrategy(NotificationType.ADOPTION_VISIT_SCHEDULED)
                    .sendNotification(visit);
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public AdoptionVisit updateStatusAndObservations(UpdateVisitDto updateVisitDto) {
        return null;
    }
}
