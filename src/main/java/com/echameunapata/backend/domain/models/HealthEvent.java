package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.health.HealthEventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "health_events")
public class HealthEvent {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @NotNull(message = "Event type cannot be null")
    @Enumerated(EnumType.STRING)
    private HealthEventType typeEvent;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String medicament;

    private String dose;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight_kg;
    private Date next_date;
    private String responsible;

    @CreationTimestamp
    private Timestamp createAt;
    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;
}
