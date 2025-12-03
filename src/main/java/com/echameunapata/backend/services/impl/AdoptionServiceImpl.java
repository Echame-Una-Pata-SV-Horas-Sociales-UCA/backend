package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.models.Adoption;
import com.echameunapata.backend.domain.models.AdoptionApplication;
import com.echameunapata.backend.domain.models.Animal;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.AdoptionRepository;
import com.echameunapata.backend.services.contract.IAdoptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdoptionServiceImpl implements IAdoptionService {

    private final AdoptionRepository adoptionRepository;

    @Override
    public void createAdoption(AdoptionApplication application) {
        try{
            var adoption = adoptionRepository.findByAdoptionApplicationId(application.getId());
            if(adoption!= null){
                return;
            }

            adoption = new Adoption();
            adoption.setAdopter(application.getPerson());
            adoption.setAnimal(application.getAnimal());
            adoption.setAdoptionApplication(application);

            adoptionRepository.save(adoption);
        }catch (HttpError e){
            throw e;
        }
    }


    @Override
    public List<Adoption> findAllAdoptions() {
        try{
            List<Adoption> adoptions = adoptionRepository.findAll();
            return  adoptions;
        }catch (HttpError e){
            throw e;
        }
    }

    @Override
    public Adoption findAdoptionById(UUID id) {
        try{
            var adoption = adoptionRepository.findById(id).orElse(null);
            if(adoption == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "This adoption not exists");
            }

            return adoption;
        }catch (HttpError e){
            throw e;
        }
    }
}
