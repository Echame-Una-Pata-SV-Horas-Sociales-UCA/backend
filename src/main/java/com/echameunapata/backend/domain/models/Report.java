package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.reports.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "complaints")
public class Report {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    @Enumerated(EnumType.STRING)
    private ReportType type;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    private String locationUrl;
    private Boolean isAnonymous;

    private String contactPhone;

    private String contactEmail;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;
    private Instant receptionDate;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Person person;

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
    private List<ReportEvidence>reportEvidences;

    @PrePersist
    public void prePersist(){
        this.status = ReportStatus.PENDING;
        this.receptionDate = Instant.now();
    }

}
