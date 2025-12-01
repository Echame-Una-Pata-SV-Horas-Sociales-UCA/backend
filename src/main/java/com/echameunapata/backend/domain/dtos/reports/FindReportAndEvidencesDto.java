package com.echameunapata.backend.domain.dtos.reports;

import com.echameunapata.backend.domain.dtos.person.PersonDto;
import com.echameunapata.backend.domain.dtos.reportEvidence.FindEvidencesDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class FindReportAndEvidencesDto extends FindReportDto {
    private List<FindEvidencesDto>evidences;
}
