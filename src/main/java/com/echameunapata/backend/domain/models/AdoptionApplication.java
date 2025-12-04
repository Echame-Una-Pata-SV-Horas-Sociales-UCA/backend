package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.adoptions.AdoptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "adoption_applications")
public class AdoptionApplication {

    @GeneratedValue(strategy= GenerationType.UUID)
    @Id
    private UUID id;

    @NotNull(message = "Animal cannot be null")
    private Instant applicationDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private AdoptionStatus status;
    private Boolean ownHome;
    private Boolean acceptsVisits;
    private String veterinarianName;
    private String veterinarianPhone;
    private Boolean commitmentToSterilization;
    private Boolean commitmentToSendPhotos;

    @Column(columnDefinition = "TEXT")
    private String observations;
    private Boolean isApplication;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @OneToOne(mappedBy = "adoptionApplication", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Adoption adoption;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptionApplication")
    private Set<AdoptionVisit> adoptionVisits;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptionApplication")
    private List<AdoptionReference> adoptionReferences;

    @PrePersist
    public void prePersist(){
        this.applicationDate =  Instant.now();
    }
}
