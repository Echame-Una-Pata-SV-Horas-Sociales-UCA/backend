package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.adoption.CreateApplicationDto;
import com.echameunapata.backend.domain.dtos.adoption.UpdateStatusInApplicationDto;
import com.echameunapata.backend.domain.models.Adoption;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface IAdoptionApplicationService {
    AdoptionApplication createApplication(CreateApplicationDto applicationDto);
    Page<AdoptionApplication> findAllApplications(String status, Instant startDate, Instant endDate, Pageable pageable);
    AdoptionApplication findApplicationById(UUID id);
    AdoptionApplication updateStatusAndDescription(UpdateStatusInApplicationDto applicationDto);

}
