package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "monitoring_adoptions")
public class AdoptionMonitoring {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String observations;

    private String photo;

    @CreationTimestamp
    private Timestamp createAt;
    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "adoption_id", nullable = false)
    private Adoption adoption;
}
