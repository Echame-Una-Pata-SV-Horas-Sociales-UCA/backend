package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.sponsorship.RegisterSponsorshipDto;
import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.sponsorship.SponsorshipStatus;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.domain.models.Sponsorship;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.SponsorshipRepository;
import com.echameunapata.backend.services.contract.IAnimalService;
import com.echameunapata.backend.services.contract.IGoogleCalendarService;
import com.echameunapata.backend.services.contract.IPersonService;
import com.echameunapata.backend.services.contract.ISponsorshipService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SponsorshipServiceImpl implements ISponsorshipService {

    private final IAnimalService animalService;
    private final IPersonService personService;
    private final SponsorshipRepository sponsorshipRepository;
    private final IGoogleCalendarService googleCalendarService;
    private final StringHttpMessageConverter stringHttpMessageConverter;

    @Scheduled(cron = "0 0 0 * * *") // todos los días a medianoche
    public void checkExpiredSponsorships() {
        validSponsorShips();
    }

    @Override
    public Sponsorship registerSponsorship(RegisterSponsorshipDto sponsorshipDto) {
        try{
            var animal = animalService.findById(sponsorshipDto.getAnimalId());
            var person = personService.createPerson(sponsorshipDto.getSponsor());

            var sponsorship  = buildSponsorship(sponsorshipDto, animal, person);

            sponsorship = sponsorshipRepository.save(sponsorship);
            schedulePayments(animal, person, sponsorship);

            return sponsorship;
        }catch (HttpError e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void schedulePayments(Animal animal, Person person, Sponsorship sponsorship) throws Exception {
        googleCalendarService.createEvent(
                "Pago mensual: " + animal.getName(),
                "Patrocinador: " + person.getFirstNames() + " " + person.getLastNames()
                        + "\nMonto: $" + sponsorship.getMonthlyAmount(),
                sponsorship.getStartDate(),
                sponsorship.getEndDate(),
                true // recurrente mensual
        );
    }

    private Sponsorship buildSponsorship(RegisterSponsorshipDto sponsorshipDto, Animal animal, Person person){
        Sponsorship sponsorship = new Sponsorship();
        sponsorship.setMonthlyAmount(sponsorshipDto.getMonthlyAmount());
        sponsorship.setStartDate(sponsorshipDto.getStartDate());
        // Always calculate endDate as startDate + 1 month
        LocalDate start = LocalDate.parse(sponsorshipDto.getStartDate());
        String endDate = start.plusMonths(1).toString();
        sponsorship.setEndDate(endDate);
        sponsorship.setNotes(sponsorshipDto.getNotes());
        sponsorship.setSponsor(person);
        sponsorship.setAnimal(animal);

        return sponsorship;
    }

    public void validSponsorShips() {
        List<Sponsorship> sponsorships = sponsorshipRepository.findAllBySponsorshipStatus(SponsorshipStatus.ACTIVE);
        LocalDate today = LocalDate.now(); // fecha actual

        sponsorships.forEach(s -> {
            LocalDate endDate = LocalDate.parse(s.getEndDate()); // convertir String → fecha
            if (endDate.isBefore(today)) {
                s.setSponsorshipStatus(SponsorshipStatus.INACTIVE);
                sponsorshipRepository.save(s);
            }
        });
    }

    @Override
    public List<Sponsorship> findAllSponsorshipByFilters(String statusString) {
        try{
            SponsorshipStatus status = (statusString != null && !statusString.isBlank()) ? SponsorshipStatus.fromString(statusString) : null;

            if(status == null){
                return sponsorshipRepository.findAll();
            }

           return sponsorshipRepository.findAllBySponsorshipStatus(status);
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public Sponsorship findSponsorshipById(UUID id) {
        try{
            var sponsorship = sponsorshipRepository.findById(id).orElse(null);
            if (sponsorship == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "This sponsorship not exists");
            }

            return sponsorship;
        }catch (HttpError e){
            throw e;
        }
    }

}
