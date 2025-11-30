package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.reports.ReportType;
import com.echameunapata.backend.domain.models.Animal;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Animal findByName(String name);
    Page<Animal>findAllByState(AnimalState state, Pageable pageable);

    @Query("""
        SELECT a FROM Animal a
        WHERE (:sex IS NULL OR a.sex = :sex)
          AND (:state IS NULL OR a.state = :state)
    """)
    Page<Animal> findAllByFilters(
            @Param("sex") AnimalSex sex,
            @Param("state") AnimalState state,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"photos"})
    @NonNull
    Optional<Animal>findById( @NonNull UUID id);
}
