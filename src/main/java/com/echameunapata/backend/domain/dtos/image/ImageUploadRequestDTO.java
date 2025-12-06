package com.echameunapata.backend.domain.dtos.image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadRequestDTO {
    @NotNull(message = "File is required. Allowed formats: JPEG, PNG, GIF, WEBP. Maximum size: 1 MB")
    private MultipartFile file;

    @NotBlank
    private String ownerType; // 'animal', 'report', 'monitoring' (usar constantes abajo)

    @NotBlank
    private String ownerId; // UUID en String
}
