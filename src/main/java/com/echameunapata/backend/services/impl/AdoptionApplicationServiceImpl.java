package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.adoption.application.CreateApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.application.UpdateStatusInApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.reference.CreateReferenceDto;
import com.echameunapata.backend.domain.enums.adoptions.AdoptionStatus;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.AdoptionReference;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AdoptionApplicationRepository;
import com.echameunapata.backend.repositories.ApplicationReferencesRepository;
import com.echameunapata.backend.services.contract.IAdoptionApplicationService;
import com.echameunapata.backend.services.contract.IAdoptionService;
import com.echameunapata.backend.services.contract.IAnimalService;
import com.echameunapata.backend.services.contract.IPersonService;
//import com.echameunapata.backend.services.notifications.factory.NotificationFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
//    private final NotificationFactory notificationFactory;
    private final IAdoptionService adoptionService;
    private final ApplicationReferencesRepository referencesRepository;

    /**
     * Crea una nueva solicitud de adopción.
     * Este método valida y construye la información necesaria para la creación de una adopción:
     * - Crea o recupera la información de la persona solicitante.
     * - Busca el animal asociado por su ID.
     * - Convierte el estado de adopción recibido en texto a su valor enumerado.
     * Finalmente, construye la entidad AdoptionApplication y la almacena en la base de datos.
     *
     * @param applicationDto información de la solicitud de adopción a crear.
     * @return La solicitud de adopción creada.
     * @throws HttpError Si el animal no existe o si ocurre un error durante el proceso.
     */
    @Override
    public AdoptionApplication createApplication(CreateApplicationDto applicationDto) {
        try{
            var person = personService.createPerson(applicationDto.getPerson());
            var animal = animalService.findById(applicationDto.getAnimalId());

            if (validateStatus(animal.getState())){
                throw new HttpError(HttpStatus.CONFLICT, "animal not available for adoption");
            }

            AdoptionStatus status = AdoptionStatus.fromString(applicationDto.getStatus());

            var application = setApplicationData(applicationDto, animal, person, status);
            AdoptionApplication newApplication = applicationRepository.save(application);

            saveAdoptionReferences(applicationDto.getReferences(), newApplication);
//            processStatusChange(application);

            // Re-fetch with relations to ensure person and animal are loaded for ModelMapper
            return findApplicationById(newApplication.getId());
        }catch (HttpError e){
            throw  e;
        }
    }

    private void saveAdoptionReferences(List<CreateReferenceDto> referencesDto, AdoptionApplication application){
        for (CreateReferenceDto r: referencesDto){
            AdoptionReference reference = new AdoptionReference();
            reference.setName(r.getName());
            reference.setPhoneNumber(r.getPhoneNumber());
            reference.setAdoptionApplication(application);
            referencesRepository.save(reference);
        }
    }

    private Boolean validateStatus(AnimalState status){
        return status.equals(AnimalState.ADOPTED)||status.equals(AnimalState.UNDER_ADOPTION);
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

    /**
     * Obtiene una lista paginada de solicitudes de adopción filtradas por estado y rango de fechas.
     *
     * Si el estado no se envía o está vacío, se obtienen solicitudes sin filtro por estado.
     * El rango de fechas es opcional y se aplica solo cuando se envían valores.
     *
     * @param status estado de la solicitud (opcional).
     * @param startDate fecha inicial del rango de búsqueda (opcional).
     * @param endDate fecha final del rango de búsqueda (opcional).
     * @return Página con las solicitudes de adopción encontradas.
     * @throws HttpError Si ocurre algún error durante la consulta.
     */
    @Override
    public List<AdoptionApplication> findAllApplications(String status, Instant startDate, Instant endDate) {
        try{
            AdoptionStatus adoptionStatus = (status != null && !status.isBlank()) ? AdoptionStatus.fromString(status) : null;
            List<AdoptionApplication> applications = applicationRepository.findApplicationsByFilters(adoptionStatus, startDate, endDate,true );

            return applications;
        }catch (HttpError e){
            throw e;
        }
    }

    /**
     * Busca una solicitud de adopción por su identificador único.
     *
     * @param id identificador único de la solicitud de adopción.
     * @return La solicitud encontrada.
     * @throws HttpError Si no existe una solicitud con ese ID.
     */
    @Override
    public AdoptionApplication findApplicationById(UUID id) {
        try{
            var adoption = applicationRepository.findWithRelationsById(id).orElse(null);
            if(adoption == null){
                throw new HttpError(HttpStatus.FOUND, "Application adoption with id not exists");
            }

            return adoption;
        }catch (HttpError e){
            throw e;
        }
    }

    /**
     * Actualiza el estado y las observaciones de una solicitud de adopción.
     *
     * Este método busca la solicitud por su ID, valida que exista y actualiza:
     * - El estado de la solicitud, convirtiéndolo desde el valor recibido en texto.
     * - Las observaciones u observaciones adicionales proporcionadas.
     * Finalmente, guarda los cambios en la base de datos.
     *
     * @param applicationDto información necesaria para actualizar el estado y las observaciones.
     * @return La solicitud de adopción con los cambios aplicados.
     * @throws HttpError Si la solicitud no existe o ocurre un error inesperado durante la actualización.
     */
    @Override
    public AdoptionApplication updateStatusAndDescription(UpdateStatusInApplicationDto applicationDto) {
        try{
            var application = findApplicationById(applicationDto.getId());

            AdoptionStatus newStatus = AdoptionStatus.fromString(applicationDto.getStatus());

            if (!newStatus.toString().equalsIgnoreCase(applicationDto.getStatus())){
                throw new HttpError(HttpStatus.BAD_REQUEST, "Invalid status value: " + applicationDto.getStatus());
            }

            validateStatusTransition(application.getStatus(), newStatus);
            application.setStatus(newStatus);

            application.setObservations(applicationDto.getObservations());

            if (AdoptionStatus.APPROVED.equals(application.getStatus())){
                application.setIsApplication(false);
            }

            applicationRepository.save(application);
            processStatusChange(application);

            // Re-fetch with relations to ensure person and animal are loaded for ModelMapper
            return findApplicationById(application.getId());
        }catch (HttpError e){
            throw e;
        }
    }

    private void validateStatusTransition(AdoptionStatus current, AdoptionStatus next) {

        // Si el estado no cambia, no hay problema
        if (current == next) return;

        // Si está aprobado o rechazado, no puede cambiar nunca
        if (current == AdoptionStatus.APPROVED) {
            throw new HttpError(HttpStatus.BAD_REQUEST,
                    "Una aplicación aprobada no puede cambiar a otro estado.");
        }

        if (current == AdoptionStatus.REJECTED) {
            throw new HttpError(HttpStatus.BAD_REQUEST,
                    "Una aplicación rechazada no puede cambiar a otro estado.");
        }

        // PENDING solo puede ir a IN_REVIEW
        if (current == AdoptionStatus.PENDING) {
            if (next != AdoptionStatus.IN_REVIEW) {
                throw new HttpError(HttpStatus.BAD_REQUEST,
                        "Una aplicación en estado PENDING solo puede pasar a IN_REVIEW.");
            }
            return;
        }

        // IN_REVIEW solo puede ir a APPROVED o REJECTED
        if (current == AdoptionStatus.IN_REVIEW) {
            if (next != AdoptionStatus.APPROVED && next != AdoptionStatus.REJECTED) {
                throw new HttpError(HttpStatus.BAD_REQUEST,
                        "Una aplicación en revisión solo puede ser APROBADA o RECHAZADA.");
            }
        }
    }


    //metodo para procesar los cambios de estado
    private void processStatusChange(AdoptionApplication application) {

        try {
            switch (application.getStatus()) {
                case APPROVED -> handleApproved(application);
                case REJECTED -> handleRejected(application);
                case IN_REVIEW -> handleInReview(application);
                case PENDING -> handleCreate(application);
                default -> { /* no action */ }
            }
        } catch (Exception e) {
            System.err.println("Error sending notification or creating adoption: " + e.getMessage());
        }
    }

    private void handleApproved(AdoptionApplication application) {

        adoptionService.createAdoption(application);
        animalService.updateAnimalStatus(application.getAnimal().getId(), AnimalState.ADOPTED);
//        notificationFactory.getStrategy(NotificationType.ADOPTION_APPLICATION_APPROVED)
//                .sendNotification(application);
    }

    private void handleRejected(AdoptionApplication application) {

        animalService.updateAnimalStatus(application.getAnimal().getId(), AnimalState.AVAILABLE);
//        notificationFactory.getStrategy(NotificationType.ADOPTION_APPLICATION_REJECTED)
//                .sendNotification(application);
    }

    private void handleCreate(AdoptionApplication application){

        animalService.updateAnimalStatus(application.getAnimal().getId(), AnimalState.UNDER_ADOPTION);
//        notificationFactory.getStrategy(NotificationType.ADOPTION_APPLICATION_REGISTERED)
//                .sendNotification(application);
    }
    private void handleInReview(AdoptionApplication application){
//        notificationFactory.getStrategy(NotificationType.ADOPTION_APPLICATION_IN_REVIEW)
//                .sendNotification(application);
    }



}
