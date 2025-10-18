package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.UserRepository;
import com.echameunapata.backend.services.contract.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Este método busca un usuario por su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return El usuario encontrado.
     * @throws HttpError Si el usuario no existe.
     */
    @Override
    public User findUserByEmail(String email) {
        try{
            var user = userRepository.findByEmail(email);
            if(user == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "User not found");
            }
            return user;
        }catch (Exception e){
            throw e;
        }
    }
}
