package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionVisitStatus;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.AdoptionVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdoptionVisitRepository extends JpaRepository<AdoptionVisit, UUID> {

    Optional<AdoptionVisit> findByAdoptionApplicationAndStatus(
            AdoptionApplication adoptionApplication,
            AdoptionVisitStatus status);
}
