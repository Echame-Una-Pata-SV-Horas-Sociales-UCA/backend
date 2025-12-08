package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.person.CreatePersonDto;
import com.echameunapata.backend.domain.dtos.person.UpdatePersonInfoDto;
import com.echameunapata.backend.domain.models.Person;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.PersonRepository;
import com.echameunapata.backend.services.contract.IPersonService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PersonServiceImpl  implements IPersonService {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public PersonServiceImpl(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Este método permite crear una nueva entidad persona
     *
     * @param personDto informacion de la nueva entidad a insertar.
     * @return La nueva entidad registrada.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public Person createPerson(CreatePersonDto personDto) {
        try{
//            var person = personRepository.findByEmail(personDto.getEmail());
//            if (person !=null && person.getEmail().equalsIgnoreCase(personDto.getEmail()) && person.getDui().equalsIgnoreCase(personDto.getDui())){
//                return person;
//            }

            var person = modelMapper.map(personDto, Person.class);
            return personRepository.save(person);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite traer todos los registro de personas creados
     *
     * @return La lista de personas encontradas.
     * @throws HttpError Error inesperado en el proceso.
     */
    @Override
    public List<Person> findAllPerson() {
        try{
            List<Person> person = personRepository.findAll();

            return person;
        }catch (Exception e){
            throw e;
        }

    }

    /**
     * Este método permite buscar el registro de una persona por email
     *
     * @param email email de la persona a buscar.
     * @return La persona encontrada.
     * @throws HttpError Si no existe un registro con ese email.
     */
    @Override
    public Person findPersonByEmail(String email) {
        try{
            var person = personRepository.findByEmail(email);
            if(person == null){
                throw new HttpError(HttpStatus.FOUND, "Person with email not exist");
            }

            return person;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite buscar el registro de una persona por id
     *
     * @param id id de la persona a buscar.
     * @return La persona encontrada.
     * @throws HttpError Si no existe un registro con ese id.
     */
    @Override
    public Person findPersonById(UUID id) {
        try{
            var person = personRepository.findById(id).orElse(null);
            if(person == null){
                throw new HttpError(HttpStatus.FOUND, "Person with id not exists");
            }
            return person;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite buscar el registro de una persona por id
     * y actualizarlo
     *
     * @param id id de la persona a actualizar.
     * @param personInfoDto informacion nueva de la persona.
     * @return La persona con los datos actualizados.
     * @throws HttpError Si no existe un registro con ese id.
     */
    @Override
    public Person updatePersonById(UUID id, UpdatePersonInfoDto personInfoDto) {
        try{
            var person = personRepository.findById(id).orElse(null);
            if(person == null){
                throw new HttpError(HttpStatus.FOUND, "Person with id not exits");
            }
            modelMapper.map(personInfoDto, person);

            return personRepository.save(person);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método permite buscar el registro de una persona por id
     * y eliminarlo
     *
     * @param id id de la persona a eliminar.
     * @throws HttpError Si no existe un registro con ese id.
     */
    @Override
    public void deletePersonById(UUID id) {
        try{
            var person = personRepository.findById(id).orElse(null);
            if(person == null){
                throw new HttpError(HttpStatus.FOUND, "Person with id not exists");
            }
            personRepository.delete(person);
        }catch (Exception e){
            throw e;
        }
    }
}
