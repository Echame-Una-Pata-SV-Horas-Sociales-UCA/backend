package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Table(name = "report_evidences")
public class ReportEvidence {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String url;
    private String description;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

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
