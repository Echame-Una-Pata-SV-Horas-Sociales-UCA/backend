package com.echameunapata.backend.domain.dtos.reports;

import com.echameunapata.backend.domain.dtos.person.PersonDto;
import com.echameunapata.backend.domain.dtos.reportEvidence.FindEvidencesDto;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class FindReportAndEvidencesDto {

    private UUID id;
    private String type;
    private String description;
    private String location;
    private String locationUrl;
    private Boolean isAnonymous;

    private String contactPhone;
    private String contactEmail;

    private String status;
    private Instant receptionDate;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private PersonDto person;
    private List<FindEvidencesDto>evidences;
}
