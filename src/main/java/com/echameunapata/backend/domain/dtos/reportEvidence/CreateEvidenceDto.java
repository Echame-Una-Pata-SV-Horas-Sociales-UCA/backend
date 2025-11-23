package com.echameunapata.backend.domain.dtos.reportEvidence;

import jakarta.mail.Multipart;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateEvidenceDto {
    @NotNull
    private List<MultipartFile> images;
    @NotEmpty(message = "Description cannot be empty")
    private String description;
}
