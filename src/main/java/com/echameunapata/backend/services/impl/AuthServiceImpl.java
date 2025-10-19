package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.auth.LoginDto;
import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.models.Role;
import com.echameunapata.backend.domain.models.Token;
import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.TokenRepository;
import com.echameunapata.backend.repositories.UserRepository;
import com.echameunapata.backend.services.contract.IAuthService;
import com.echameunapata.backend.services.contract.IRoleService;
import com.echameunapata.backend.utils.token.JwtTools;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;
    private final JwtTools jwtTools;
    private final TokenRepository tokenRepository;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, IRoleService roleService, JwtTools jwtTools, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtTools = jwtTools;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Este método registra un nuevo usuario.
     *
     * @param userDto El DTO del usuario a registrar.
     * @throws HttpError Si el usuario ya existe.
     */
        @Override
        @Transactional(rollbackOn = Exception.class)
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
                Role role = roleService.findById("USER");

                user.setRoles(new HashSet<>(Set.of(role)));

                return userRepository.save(user);
            }catch (Exception e){
                throw e;
            }
        }

    /**
     * Este método permite a un usuario iniciar sesión.
     *
     * @param userDto Contiene las credenciales de inicio de sesion del usuario (email, password)
     * @return El token de acceso del usuario.
     * @throws HttpError Si las credenciales son inválidas.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Token loginUser(LoginDto userDto) {
        try{
            var user = userRepository.findByEmail(userDto.getEmail());

            if(user == null || !passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
                throw new HttpError(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

            return registerToken(user);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * Este método registra un nuevo token para un usuario.
     *
     * @param user El usuario para el que se registra el token.
     * @return El nuevo token registrado.
     * @throws HttpError Si el token no es válido.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Token registerToken(User user) {
        try {
            String tokenString = jwtTools.generateToken(user);
            Token token = new Token(tokenString, user);
            tokenRepository.save(token);

            return token;
        }catch (Exception e){
            throw e;
        }

    }

    /**
     * Este método verifica si un token es válido.
     *
     * @param user  El usuario al que pertenece el token.
     * @param token El token a verificar.
     * @return true si el token es válido, false en caso contrario.
     * @throws HttpError Si el token no es válido.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean isTokenValid(User user, String token) {
        try {
            cleanToken(user);
            List<Token> tokens = tokenRepository.findByUserAndCanActive(user, true);
            tokens.stream()
                    .map(t -> t.getToken().equals(token))
                    .findAny()
                    .orElseThrow(() -> new HttpError(HttpStatus.UNAUTHORIZED, "Token is not valid"));
            return true;
        } catch (HttpError e) {
            throw e;
        }
    }

    /**
     * Este método limpia los tokens inactivos de un usuario.
     *
     * @param user El usuario del que se limpian los tokens.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void cleanToken(User user) {
        List<Token>token = tokenRepository.findByUserAndCanActive(user, true);
        token.forEach(t -> {
            if(!jwtTools.verifyTokens(t.getToken())){
                t.setCanActive(false);
                tokenRepository.save(t);
            }
        });
    }

    /**
     * Este método obtiene el usuario autenticado.
     *
     * @return El usuario autenticado.
     */
    @Override
    public User findUserAuthenticated() {
        try{
            String email = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();


            return userRepository.findByEmail(email);
        }catch (HttpError e){
            throw e;
        }
    }


}
