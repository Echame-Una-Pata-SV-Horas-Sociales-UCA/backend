package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Animal findByName(String name);
}
