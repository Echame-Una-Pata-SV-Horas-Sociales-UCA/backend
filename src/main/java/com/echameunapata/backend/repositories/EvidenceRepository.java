package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.ReportEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenceRepository extends JpaRepository<ReportEvidence, UUID> {
    List<ReportEvidence> findALlByReportId(UUID reportId);
}
