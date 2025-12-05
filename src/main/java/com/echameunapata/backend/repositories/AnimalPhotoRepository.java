package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.AnimalPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnimalPhotoRepository extends JpaRepository<AnimalPhoto, UUID> {
    List<AnimalPhoto> findAllById(UUID id);
}
