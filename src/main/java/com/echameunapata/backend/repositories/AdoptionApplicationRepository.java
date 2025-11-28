package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionStatus;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdoptionApplicationRepository extends JpaRepository<AdoptionApplication, UUID> {

    @EntityGraph(attributePaths = {"person", "animal"})
    @Query("""
        SELECT a FROM AdoptionApplication a
        WHERE (:status IS NULL OR a.status = :status)
            AND (COALESCE(:startDate, a.applicationDate) <= a.applicationDate)
            AND(COALESCE(:endDate, a.applicationDate) >= a.applicationDate)
    """)
    Page<AdoptionApplication>findApplicationsByFilters(
            @Param("status") AdoptionStatus status,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"person", "animal"})
    Optional<AdoptionApplication> findWithRelationsById(UUID id);

}
