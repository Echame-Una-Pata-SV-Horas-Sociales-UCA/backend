package com.echameunapata.backend.domain.dtos.reports;

import com.echameunapata.backend.domain.dtos.person.PersonDto;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
public class FindReportDto {

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
}
