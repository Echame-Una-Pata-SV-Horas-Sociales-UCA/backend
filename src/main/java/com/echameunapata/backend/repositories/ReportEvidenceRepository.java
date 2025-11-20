package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.ReportEvidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportEvidenceRepository extends JpaRepository<ReportEvidence, UUID> {
    List<ReportEvidence> findALlById(UUID id);
}
