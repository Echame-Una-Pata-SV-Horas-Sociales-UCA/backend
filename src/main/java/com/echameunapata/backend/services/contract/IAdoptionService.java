package com.echameunapata.backend.services.contract;


import com.echameunapata.backend.domain.models.Adoption;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IAdoptionService {
    void createAdoption(AdoptionApplication application);
    Page<Adoption>  findAllAdoptions(Pageable pageable);
    Adoption findAdoptionById(UUID id);

}
