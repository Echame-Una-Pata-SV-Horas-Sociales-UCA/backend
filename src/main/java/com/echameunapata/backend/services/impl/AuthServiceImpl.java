package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.UserRepository;
import com.echameunapata.backend.services.contract.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(RegisterUserDto userDto) {
        try {
            var user = userRepository.findByEmail(userDto.getEmail());

            if(user != null){
                throw new HttpError(HttpStatus.CONFLICT, "Email already in use");
            }

            user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            return userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }


}
