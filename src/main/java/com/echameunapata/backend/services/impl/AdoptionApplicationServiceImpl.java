package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.adoption.CreateApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.UpdateStatusInApplicationDto;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdoptionApplicationServiceImpl implements IAdoptionApplicationService {

    private final IAnimalService animalService;
    private final IPersonService personService;
    private final AdoptionApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    /**
     * Crea una nueva solicitud de adopción.
     *
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

    /**
     * Obtiene una lista paginada de solicitudes de adopción filtradas por estado y rango de fechas.
     *
     * Si el estado no se envía o está vacío, se obtienen solicitudes sin filtro por estado.
     * El rango de fechas es opcional y se aplica solo cuando se envían valores.
     *
     * @param status estado de la solicitud (opcional).
     * @param startDate fecha inicial del rango de búsqueda (opcional).
     * @param endDate fecha final del rango de búsqueda (opcional).
     * @param pageable información de paginación.
     * @return Página con las solicitudes de adopción encontradas.
     * @throws HttpError Si ocurre algún error durante la consulta.
     */
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
            var adoption = findApplicationById(applicationDto.getId());
            AdoptionStatus status = AdoptionStatus.fromString(applicationDto.getStatus());

            validStatusTransition(adoption.getStatus(), status);
            adoption.setStatus(status);

            String updatedObs = appendObservationHistory(
                    adoption.getObservations(),
                    applicationDto.getObservations(),
                    status
            );

            adoption.setObservations(updatedObs);

            return applicationRepository.save(adoption);
        }catch (HttpError e){
            throw e;
        }
    }

    private void validStatusTransition(AdoptionStatus oldStatus, AdoptionStatus newStatus) {

        // --- 1. Estados finales donde NO se permite ningún cambio ---
        if (oldStatus == AdoptionStatus.DELIVERED ||
                oldStatus == AdoptionStatus.FOLLOW_UP ||
                oldStatus == AdoptionStatus.REJECTED) {

            throw new HttpError(HttpStatus.BAD_REQUEST,
                    "No se puede cambiar el estado de una solicitud finalizada o rechazada");
        }

        // --- 2. No permitir regresiones de estado ---
        Map<AdoptionStatus, Integer> order = Map.of(
                AdoptionStatus.PENDING, 1,
                AdoptionStatus.IN_REVIEW, 2,
                AdoptionStatus.APPROVED, 3,
                AdoptionStatus.DELIVERED, 4,
                AdoptionStatus.FOLLOW_UP, 5
        );

        if (order.get(newStatus) < order.get(oldStatus)) {
            throw new HttpError(HttpStatus.BAD_REQUEST,
                    "No se puede retroceder el estado de la solicitud");
        }

        // No permitir entregar si no está aprobada
        if (oldStatus != AdoptionStatus.APPROVED &&
                newStatus == AdoptionStatus.DELIVERED) {

            throw new HttpError(HttpStatus.BAD_REQUEST,
                    "Solo se puede entregar una mascota cuando la solicitud ha sido aprobada");
        }

        // No permitir pasar a seguimiento si no se entregó primero
        if (oldStatus != AdoptionStatus.DELIVERED &&
                newStatus == AdoptionStatus.FOLLOW_UP) {

            throw new HttpError(HttpStatus.BAD_REQUEST,
                    "Solo se puede pasar a seguimiento después de la entrega");
        }
    }


    private String appendObservationHistory(String currentObservations, String newComment, AdoptionStatus newStatus) {
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.of("UTC"))
                .format(Instant.now());

        String entry = String.format(
                "[%s] Estado cambiado a %s → \"%s\"",
                timestamp,
                newStatus.name(),
                newComment
        );

        if (currentObservations == null || currentObservations.isBlank()) {
            return entry;
        }

        return currentObservations + "\n\n" + entry;
    }

}
