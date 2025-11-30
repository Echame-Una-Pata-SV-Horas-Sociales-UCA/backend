package com.echameunapata.backend.domain.dtos.image;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
public class FindImagesDto {
    private String contentType;
    private String url;
    private Boolean isPrimary;
    private Timestamp createdAt;
}
