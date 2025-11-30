package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.Adoption;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, UUID> {
    Adoption findByAdoptionApplicationId(UUID adoptionApplicationID);

    @EntityGraph(attributePaths = {"adopter", "animal", "animal.photos", "adoptionApplication"})
    @NonNull
    Page<Adoption>findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"adopter", "animal", "animal.photos", "adoptionApplication"})
    @NonNull
    Optional<Adoption>findById(@NonNull UUID id);
}
