package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
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

}
