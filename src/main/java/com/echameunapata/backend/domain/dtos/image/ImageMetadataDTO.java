package com.echameunapata.backend.domain.dtos.image;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ImageMetadataDTO {
    private UUID id;
    private String secureUrl;
    private String thumbnailUrl;
    private String provider;
    private String providerPublicId;
    private String contentType;
    private Long sizeBytes;
    private OffsetDateTime createdAt;
}
