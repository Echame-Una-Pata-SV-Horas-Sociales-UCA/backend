package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.AdoptionMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdoptionMonitoringRepository extends JpaRepository<AdoptionMonitoring, UUID> {
    List<AdoptionMonitoring> findAllById(UUID id);
}
