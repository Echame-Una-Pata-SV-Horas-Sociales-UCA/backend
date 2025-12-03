package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.Adoption;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, UUID> {
    Adoption findByAdoptionApplicationId(UUID adoptionApplicationID);

    @EntityGraph(attributePaths = {"adopter", "animal", "adoptionApplication"})
    @NonNull
    List<Adoption> findAll();

    @EntityGraph(attributePaths = {"adopter", "animal", "animal.photos", "adoptionApplication"})
    @NonNull
    Optional<Adoption>findById(@NonNull UUID id);
}
