package com.echameunapata.backend.domain.dtos.reports;

import com.echameunapata.backend.domain.dtos.person.CreatePersonDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateReportDto {


    private String type;
    private String description;
    private String location;
    private String locationUrl;
    private Boolean isAnonymous;

    private String contactPhone;

    private String contactEmail;

    private CreatePersonDto person;
    @NotNull(message = "Photo is required. Allowed formats: JPEG, PNG, GIF, WEBP. Maximum size: 1 MB")
    private MultipartFile photo;
}
