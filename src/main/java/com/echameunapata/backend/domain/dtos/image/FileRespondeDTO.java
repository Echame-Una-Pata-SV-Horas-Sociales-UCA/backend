package com.echameunapata.backend.domain.dtos.image;

import lombok.Data;

import java.util.UUID;

@Data
public class FileRespondeDTO {
    private UUID id;
    private String url;           // secureUrl
    private String thumbnailUrl;
    private String publicId;      // providerPublicId
    private String type;          // contentType
    private Long size;             // sizeBytes
}
