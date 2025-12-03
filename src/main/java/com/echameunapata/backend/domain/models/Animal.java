package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalSpecies;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
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
    private String name;

    @Enumerated(EnumType.STRING)
    private AnimalSpecies species;

    @Enumerated(EnumType.STRING)
    private AnimalSex sex;
    private String race;
    private String age;

    @Enumerated(EnumType.STRING)
    private AnimalState state = AnimalState.AVAILABLE;
    private LocalDate rescueDate;
    private String rescueLocation;

    @Column(columnDefinition = "TEXT")
    private String initialDescription;
    private Boolean sterilized =false;
    private Boolean missingLimb = false;
    @Column(columnDefinition = "TEXT")
    private String observations;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    private String photo;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<AdoptionApplication> adoptionApplications;

    @OneToOne(mappedBy = "animal", fetch = FetchType.LAZY)
    private Adoption adoption;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<HealthEvent>healthEvents= new ArrayList<>();

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<Sponsorship>sponsorships= new ArrayList<>();

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TemporaryHome>temporaryHomes = new ArrayList<>();
}
