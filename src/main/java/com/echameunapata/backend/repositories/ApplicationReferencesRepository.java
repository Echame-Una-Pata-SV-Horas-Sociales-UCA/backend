package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.AdoptionReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationReferencesRepository extends JpaRepository<AdoptionReference, UUID> {
}
