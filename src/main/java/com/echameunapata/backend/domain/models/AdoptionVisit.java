package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionVisitResult;
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

    private Date date;

    @NotNull(message = "Result cannot be null")
    @Enumerated(EnumType.STRING)
    private AdoptionVisitResult result;

    @Column( columnDefinition = "TEXT")
    private String observations;

    @NotEmpty(message = "Evaluator name cannot be null")
    private String evaluatorName;

    @CreationTimestamp
    private Timestamp createAt;
    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_application_id", nullable = false)
    private AdoptionApplication adoptionApplication;

}
