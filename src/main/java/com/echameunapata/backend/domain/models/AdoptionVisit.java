package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionVisitStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "adoption_visits")
public class AdoptionVisit {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    private String scheduledDate;

    @Enumerated(EnumType.STRING)
    private AdoptionVisitStatus status = AdoptionVisitStatus.SCHEDULED;

    @Column( columnDefinition = "TEXT")
    private String observations;

    private String evaluatorName;

    @CreationTimestamp
    private Timestamp createAt;
    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_application_id", nullable = false)
    private AdoptionApplication adoptionApplication;

}
