package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "report_evidences")
public class ReportEvidence {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String url;
    private String description;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;
}
