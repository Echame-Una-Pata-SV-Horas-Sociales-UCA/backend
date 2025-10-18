package com.echameunapata.backend.domain.models;

import com.echameunapata.backend.domain.enums.temporaryHomes.TemporaryHomesStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "temporary_homes")
public class TemporaryHome {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TemporaryHomesStatus temporaryHomeStatus;
    private Date startDate;
    private Date endDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
