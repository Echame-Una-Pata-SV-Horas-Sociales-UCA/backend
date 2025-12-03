package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.reports.CreateReportDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateReportInfoDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateStatusReportDto;
import com.echameunapata.backend.domain.models.Report;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface IReportService {
    Report createReport(CreateReportDto reportDto);
    Report findReportById(UUID id);
    Report updateStatusReport(UpdateStatusReportDto reportDto);
    Report updateReport(UUID id, UpdateReportInfoDto reportInfoDto);
    void deleteOneReport(UUID id);
    List<Report>findAllReportsByFilters(String type, String status, Instant startDate, Instant endDate);
}
