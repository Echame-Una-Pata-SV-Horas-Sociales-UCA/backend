package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.adoption.CreateApplicationDto;
import com.echameunapata.backend.domain.enums.adoptions.AdoptionStatus;
import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AdoptionApplicationRepository;
import com.echameunapata.backend.services.contract.IAdoptionApplicationService;
import com.echameunapata.backend.services.contract.IAnimalService;
import com.echameunapata.backend.services.contract.IPersonService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdoptionApplicationServiceImpl implements IAdoptionApplicationService {

    private final IAnimalService animalService;
    private final IPersonService personService;
    private final AdoptionApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    @Override
    public AdoptionApplication createApplication(CreateApplicationDto applicationDto) {
        try{
            var person = personService.createPerson(applicationDto.getPerson());
            var animal = animalService.findById(applicationDto.getAnimalId());
            AdoptionStatus status = AdoptionStatus.fromString(applicationDto.getStatus());

            var application = setApplicationData(applicationDto, animal, person, status);
            return applicationRepository.save(application);
        }catch (HttpError e){
            throw  e;
        }
    }

    private AdoptionApplication setApplicationData(CreateApplicationDto applicationDto, Animal animal, Person person, AdoptionStatus status){
        var application = new AdoptionApplication();

        application.setStatus(status);
        application.setOwnHome(applicationDto.getOwnHome());
        application.setAcceptsVisits(applicationDto.getAcceptsVisits());
        application.setVeterinarianName(applicationDto.getVeterinarianName());
        application.setVeterinarianPhone(applicationDto.getVeterinarianPhone());
        application.setCommitmentToSterilization(applicationDto.getCommitmentToSterilization());
        application.setCommitmentToSendPhotos(applicationDto.getCommitmentToSendPhotos());
        application.setPerson(person);
        application.setAnimal(animal);

        return application;
    }

    @Override
    public Page<AdoptionApplication> findAllApplications(String status, Instant startDate, Instant endDate, Pageable pageable) {
        try{
            AdoptionStatus adoptionStatus = (status != null && !status.isBlank()) ? AdoptionStatus.fromString(status) : null;
            Page<AdoptionApplication> applications = applicationRepository.findApplicationsByFilters(adoptionStatus, startDate, endDate, pageable);

            return applications;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public AdoptionApplication findApplicationById(UUID id) {
        try{
            var adoption = applicationRepository.findById(id).orElse(null);
            if(adoption == null){
                throw new HttpError(HttpStatus.FOUND, "Application adoption with id not exists");
            }

            return adoption;
        }catch (HttpError e){
            throw e;
        }
    }
}
