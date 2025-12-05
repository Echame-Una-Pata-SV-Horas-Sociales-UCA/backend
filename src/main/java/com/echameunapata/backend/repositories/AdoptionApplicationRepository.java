package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionStatus;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, UUID> {

    @EntityGraph(attributePaths = {"person", "animal"})
    @Query("""
        SELECT a FROM AdoptionApplication a
        WHERE (:isApplication IS NULL OR a.isApplication = :isApplication)
          AND (:status IS NULL OR a.status = :status)
          AND (COALESCE(:startDate, a.applicationDate) <= a.applicationDate)
          AND (COALESCE(:endDate, a.applicationDate) >= a.applicationDate)
        ORDER BY 
          CASE a.status
            WHEN 'PENDING' THEN 1
            WHEN 'IN_REVIEW' THEN 2
            WHEN 'ACCEPTED' THEN 3
            WHEN 'REJECTED' THEN 4
            ELSE 5
          END,
          a.applicationDate DESC
    """)
    List<AdoptionApplication> findApplicationsByFilters(
            @Param("status") AdoptionStatus status,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("isApplication") Boolean isApplication
    );

    @EntityGraph(attributePaths = {"person", "animal", "adoptionVisits", "adoptionReferences"})
    Optional<AdoptionApplication> findWithRelationsById(UUID id);

}
