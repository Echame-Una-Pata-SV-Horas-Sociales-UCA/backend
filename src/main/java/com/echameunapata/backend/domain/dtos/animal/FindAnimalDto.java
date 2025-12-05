package com.echameunapata.backend.domain.dtos.animal;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class FindAnimalDto {

    private UUID id;
    private String name;
    private String species;
    private String state;

    private String sex;
    private String race;
    private String age;
    private String photo;
    private Boolean sterilized;
    private Boolean missingLimb;
    private String rescueLocation;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String initialDescription;
}
