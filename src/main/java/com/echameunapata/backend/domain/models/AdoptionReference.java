package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "adoption_references")
public class AdoptionReference {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    private String name;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplication_id", nullable = false)
    private AdoptionApplication adoptionApplication;
}
