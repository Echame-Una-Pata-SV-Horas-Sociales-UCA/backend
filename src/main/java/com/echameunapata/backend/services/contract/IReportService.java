package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.reports.CreateReportDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateReportInfoDto;
import com.echameunapata.backend.domain.dtos.reports.UpdateStatusReportDto;
import com.echameunapata.backend.domain.models.Report;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface IReportService {
    Report createReport(CreateReportDto reportDto) throws IOException;
    Report findReportById(UUID id);
    Report updateStatusReport(UpdateStatusReportDto reportDto);
    void deleteOneReport(UUID id);
    List<Report>findAllReportsByFilters(String type, String status, Instant startDate, Instant endDate);
}
