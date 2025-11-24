package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.reportEvidence.CreateEvidenceDto;
import com.echameunapata.backend.domain.models.ReportEvidence;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IEvidenceService {
    void createEvidence(CreateEvidenceDto evidenceDto, UUID reportId) throws IOException;
    List<ReportEvidence>findAllEvidencesByReport(UUID reportId);
    void deleteEvidence(UUID id) throws IOException;
    ReportEvidence findEvidenceById(UUID id);
}
