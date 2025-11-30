package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.adoption.application.visit.ProgramingVisitDto;
import com.echameunapata.backend.domain.dtos.adoption.application.visit.UpdateVisitDto;
import com.echameunapata.backend.domain.enums.adoptions.AdoptionStatus;
import com.echameunapata.backend.domain.enums.adoptions.AdoptionVisitStatus;
import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.AdoptionVisit;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AdoptionVisitRepository;
import com.echameunapata.backend.services.contract.IAdoptionApplicationService;
import com.echameunapata.backend.services.contract.IAdoptionVisitService;
import com.echameunapata.backend.services.notifications.factory.NotificationFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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
            var visit = adoptionVisitRepository.findByAdoptionApplicationAndStatus(application, AdoptionVisitStatus.SCHEDULED).orElse(null);

            // si ya hay visita agendada no se debe crear otra
            if(visit != null){
                throw new HttpError(HttpStatus.CONFLICT, "Visit already programing");
            }

            //si la aplicacion a adopcion no esta en revision no hay que agendar cita aun
            if(!AdoptionStatus.IN_REVIEW.equals(application.getStatus()) && application.getAcceptsVisits()){
                throw new HttpError(HttpStatus.NOT_FOUND, "Invalid status in application for programing visit");
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
        try{
            var adoptionVisit = adoptionVisitRepository.findById(updateVisitDto.getId()).orElse(null);
            if(adoptionVisit == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "Invalid visit");
            }
            validateVisitStateTransition(adoptionVisit.getStatus(), updateVisitDto.getStatus());

            adoptionVisit.setObservations(updateVisitDto.getObservations());
            adoptionVisit.setStatus(updateVisitDto.getStatus());

            return adoptionVisitRepository.save(adoptionVisit);
        }catch (HttpError e){
            throw e;
        }
    }

    private void validateVisitStateTransition(AdoptionVisitStatus current, AdoptionVisitStatus next) {

        if (current == AdoptionVisitStatus.COMPLETED) {
            throw new HttpError(HttpStatus.CONFLICT,
                    "A completed visit cannot change its state");
        }

        if (current == AdoptionVisitStatus.CANCELLED) {
            throw new HttpError(HttpStatus.CONFLICT,
                    "A canceled visit cannot change its state");
        }

        if (current == AdoptionVisitStatus.SCHEDULED) {
            if (next != AdoptionVisitStatus.COMPLETED && next != AdoptionVisitStatus.CANCELLED) {
                throw new HttpError(HttpStatus.CONFLICT,
                        "Scheduled visits can only be completed or canceled");
            }
            return;
        }

        throw new HttpError(HttpStatus.BAD_REQUEST, "Invalid visit state transition");
    }

}
