package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.models.Role;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.RoleRepository;
import com.echameunapata.backend.services.contract.IRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Este método busca un rol por su ID.
     *
     * @param id El ID del rol a buscar.
     * @return El rol encontrado.
     * @throws HttpError Si el rol no existe.
     */
    @Override
    public Role findById(String id) {
        try{
            var role = roleRepository.findById(id).orElse(null);
            if (role == null)
            {
                throw  new HttpError(HttpStatus.NOT_FOUND, "Role not exists");
            }
            return role;
        }catch (Exception e){
            throw  e;
        }
    }

    /**
     * Este método permite trear todos los roles almacenados.
     *
     * @return La lista de roles almacenada.
     * @throws HttpError Si la lista esta vacia.
     */
    @Override
    public List<Role> findAllRoles() {
        try{
            var roles = roleRepository.findAll();
            if (roles.isEmpty()){
                throw new HttpError(HttpStatus.FOUND, "Role list is empty");
            }
            return roles;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método busca una lista de roles por sus IDs.
     *
     * @param ids La lista de IDs de los roles a buscar.
     * @return La lista de roles encontrados.
     * @throws HttpError En excepciones internas del ide.
     */
    @Override
    public List<Role> findAllByIds(List<String> ids) {
        try{
            Iterable<Role>lisRoles = roleRepository.findAllById(ids);
            List<Role>roles = new ArrayList<>();
            lisRoles.forEach(roles::add);

            return roles;
        }catch (Exception e){
            throw e;
        }
    }
}
