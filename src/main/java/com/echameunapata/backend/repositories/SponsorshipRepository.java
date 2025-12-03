package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.sponsorship.SponsorshipStatus;
import com.echameunapata.backend.domain.models.Sponsorship;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, UUID> {

    @EntityGraph(attributePaths = {"sponsor", "animal"})
    List<Sponsorship>findAllBySponsorshipStatus(SponsorshipStatus sponsorshipStatus);

    @EntityGraph(attributePaths = {"sponsor", "animal"})
    @NonNull
    List<Sponsorship>findAll();

    @EntityGraph(attributePaths = {"sponsor", "animal", "animal.photos"})
    @NonNull
    Optional<Sponsorship> findById(@NonNull UUID id);

}
