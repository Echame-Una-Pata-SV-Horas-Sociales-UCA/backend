package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "adoptions")
public class Adoption {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private LocalDate deliveryDate;
    private Boolean active_monitoring;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne(optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person adopter;

    @OneToOne(optional = false)
    @JoinColumn(name = "adoption_application_id", nullable = false, unique = true)
    private AdoptionApplication adoptionApplication;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AdoptionMonitoring>adoptionMonitoring;



}
