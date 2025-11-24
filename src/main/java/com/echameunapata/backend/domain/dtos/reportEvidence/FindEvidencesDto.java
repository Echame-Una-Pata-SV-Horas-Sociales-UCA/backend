package com.echameunapata.backend.domain.dtos.reportEvidence;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class FindEvidencesDto {

    private UUID id;
    private String url;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
