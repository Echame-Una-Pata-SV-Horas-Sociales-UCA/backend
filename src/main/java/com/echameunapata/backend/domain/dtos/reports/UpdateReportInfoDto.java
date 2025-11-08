package com.echameunapata.backend.domain.dtos.reports;


import com.echameunapata.backend.domain.enums.reports.ReportType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdateReportInfoDto {

    @NotNull(message = "Complaint type cannot be null")
    private ReportType type;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    private String locationUrl;

    private String contactPhone;

    @Email(message = "Email should be valid")
    private String contactEmail;

}
