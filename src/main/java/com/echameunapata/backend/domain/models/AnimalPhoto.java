package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "animal_photos")
public class AnimalPhoto {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String url;
    private Boolean isPrimary;
    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

}
