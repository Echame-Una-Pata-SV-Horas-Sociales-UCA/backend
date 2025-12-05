package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.person.CreatePersonDto;
import com.echameunapata.backend.domain.dtos.person.UpdatePersonInfoDto;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.domain.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IPersonService {
    Person createPerson(CreatePersonDto personDto);
    List<Person> findAllPerson();
    Person findPersonByEmail(String email);
    Person findPersonById(UUID id);
    Person updatePersonById(UUID id, UpdatePersonInfoDto personInfoDto);
    void deletePersonById(UUID id);
}
