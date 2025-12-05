package com.echameunapata.backend.domain.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "tokens")
@NoArgsConstructor
public class Token {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String token;
    private Boolean canActive;
    private String type;
    private Date timestamp;

    @PrePersist
    public void prePersist(){
        this.timestamp = Date.from(Instant.now());
        this.canActive = true;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Token(String token , User user){
        this.token = token;
        this.user = user;
    }

}
