package com.echameunapata.backend.domain.dtos.reportEvidence;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateEvidenceDto {
    @NotNull(message = "Image file is required")
    private MultipartFile image;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
}
