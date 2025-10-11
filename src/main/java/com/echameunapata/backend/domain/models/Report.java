package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.ReportStatus;
import com.echameunapata.backend.domain.enums.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
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
    @NotNull(message = "Complaint type cannot be null")
    private ReportType type;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    private String locationUrl;
    private Boolean isAnonymous;

    private String contactPhone;

    @Email(message = "Email should be valid")
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Complaint status cannot be null")
    private ReportStatus status;
    private Timestamp receptionDate;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Person person;

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
    private List<ReportEvidence>reportEvidences;

}
