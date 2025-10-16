package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalSpecies;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "animals")
public class Animal {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Species cannot be empty")
    @Enumerated(EnumType.STRING)
    private AnimalSpecies species;

    @NotNull(message = "Sex cannot be null")
    @Enumerated(EnumType.STRING)
    private AnimalSex sex;
    private String race;
    private LocalDate birthDate;

    private String state;
    private LocalDate rescueDate;
    private String rescueLocation;

    @Column(columnDefinition = "TEXT")
    private String initialDescription;
    private Boolean sterilized;
    private Boolean missingLimb;
    @Column(columnDefinition = "TEXT")
    private String observations;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AnimalPhoto>photos = new ArrayList<>();

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<AdoptionApplication> adoptionApplications;

    @OneToOne(mappedBy = "animal", fetch = FetchType.LAZY)
    private Adoption adoption;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<HealthEvent>healthEvents;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<Sponsorship>sponsorships;
}
