package com.echameunapata.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    private String id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    private String description;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Set<User> users;

}
