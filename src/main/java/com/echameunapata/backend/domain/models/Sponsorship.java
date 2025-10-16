package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.sponsorship.SponsorshipStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "sponsorships")
public class Sponsorship {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal monthly_amount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Sponsorship status cannot be null")
    private SponsorshipStatus sponsorship_status;

    private Date start_date;

    private Date end_date;
    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    private Timestamp createAt;
    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sponsor_id", nullable = false)
    private Person sponsor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;
}
