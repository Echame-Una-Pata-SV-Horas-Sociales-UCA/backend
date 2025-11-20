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

    /* --- Campos nuevos para integración Cloudinary / manejo robusto --- */

    /**
     * Nombre del proveedor (p. ej. "cloudinary").
     */
    private String provider;

    /**
     * Public ID devuelto por el proveedor (Cloudinary public_id).
     */
    @Column(name = "provider_public_id", length = 255)
    private String providerPublicId;

    /**
     * URL segura (HTTPS) devuelta por Cloudinary.
     */
    @Column(name = "secure_url", columnDefinition = "TEXT")
    private String secureUrl;

    /**
     * URL de thumbnail/miniatura (opcional).
     */
    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    /**
     * Tipo MIME (image/jpeg, image/png).
     */
    @Column(name = "content_type", length = 100)
    private String contentType;

    /**
     * Tamaño en bytes del objeto subido.
     */
    @Column(name = "size_bytes")
    private Long sizeBytes;
}