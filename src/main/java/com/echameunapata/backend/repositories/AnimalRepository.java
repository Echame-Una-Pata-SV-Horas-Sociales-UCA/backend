package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Animal findByName(String name);
    List<Animal>findAllByState(AnimalState state, Pageable pageable);
    List<Animal>findAll(Pageable pageable);
    
}
