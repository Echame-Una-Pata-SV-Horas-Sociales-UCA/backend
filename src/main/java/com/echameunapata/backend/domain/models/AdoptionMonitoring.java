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

    /**
     * Campo legacy (si existía con un solo String). Mantener por compatibilidad,
     * pero ahora preferimos secureUrl / providerPublicId.
     */
    private String photo;

    @CreationTimestamp
    private Timestamp createAt;
    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "adoption_id", nullable = false)
    private Adoption adoption;

    /* --- Campos nuevos para Cloudinary --- */
    private String provider;

    @Column(name = "provider_public_id", length = 255)
    private String providerPublicId;

    @Column(name = "secure_url", columnDefinition = "TEXT")
    private String secureUrl;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "size_bytes")
    private Long sizeBytes;
}
