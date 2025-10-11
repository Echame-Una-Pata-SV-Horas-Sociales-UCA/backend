package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "persons")
public class Person {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    @NotEmpty(message = "First names cannot be empty")
    @Min(value = 3, message = "First names must be at least 3 characters long")
    @Max(value = 50, message = "First names cannot be longer than 50 characters")
    private String firstNames;
    @NotEmpty(message = "Last names cannot be empty")
    @Min(value = 3, message = "Last names must be at least 3 characters long")
    @Max(value = 50, message = "Last names cannot be longer than 50 characters")
    private String lastNames;
    private String phoneNumber;
    @NotEmpty(message = "DUI cannot be empty")
    @Pattern(regexp = "\\d{8}-\\d", message = "DUI must be in the format ########-#")
    private String dui;
    private String address;
    private String city;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Report> complaints;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<AdoptionApplication> adoptionApplications;
}
