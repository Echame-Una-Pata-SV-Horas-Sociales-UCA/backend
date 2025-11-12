package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.models.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Animal findByName(String name);
    Page<Animal>findAllByState(AnimalState state, Pageable pageable);
    Page<Animal> findAll(Pageable pageable);
    
}
