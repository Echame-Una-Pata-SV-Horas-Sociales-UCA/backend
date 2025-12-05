package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.animals.AnimalSex;
import com.echameunapata.backend.domain.enums.animals.AnimalState;
import com.echameunapata.backend.domain.models.Animal;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    long countByState(AnimalState state);

    @Query("SELECT YEAR(a.rescueDate) as year, COUNT(a) as count FROM Animal a GROUP BY YEAR(a.rescueDate)")
    List<Object[]> countRescuesByYear();

    long countByAdoptionIsNotNull();

    Animal findByName(String name);
    List<Animal>findAllByState(AnimalState state);

    @Query("SELECT a FROM Animal a WHERE " +
            "(:sex IS NULL OR a.sex = :sex) AND " +
            "(:state IS NULL OR a.state = :state)")
    List<Animal> findAllByFilters(@Param("sex") AnimalSex sex, @Param("state") AnimalState state);
}
