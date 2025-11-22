package com.echameunapata.backend.domain.dtos.reports;

import com.echameunapata.backend.domain.dtos.person.CreatePersonDto;
import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.reports.ReportType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CreateReportDto {


    @NotNull(message = "Complaint type cannot be null")
    private String type;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    private String locationUrl;
    private Boolean isAnonymous;

    private String contactPhone;

    @Email(message = "Email should be valid")
    private String contactEmail;

    private CreatePersonDto person;
}
