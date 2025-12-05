package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.auth.LoginDto;
import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.enums.notifications.NotificationType;
import com.echameunapata.backend.domain.models.Role;
import com.echameunapata.backend.domain.models.Token;
import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.TokenRepository;
import com.echameunapata.backend.repositories.UserRepository;
import com.echameunapata.backend.services.contract.IAuthService;
import com.echameunapata.backend.services.contract.IMailService;
import com.echameunapata.backend.services.contract.IRoleService;
import com.echameunapata.backend.services.notifications.factory.NotificationFactory;
import com.echameunapata.backend.utils.token.JwtTools;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private static final String TOKEN_TYPE_PASSWORD_RESET = "PASSWORD_RESET";
    private static final Duration RESET_TOKEN_TTL = Duration.ofHours(1);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleService roleService;
    private final JwtTools jwtTools;
    private final TokenRepository tokenRepository;

    private final IMailService mailService;
    private final NotificationFactory notificationFactory;

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
     * Este método maneja la solicitud de reseteo de contraseña.
     * @param email El correo electrónico del usuario que solicita el reseteo.
     */
    @Override
    @Transactional(rollbackOn =  Exception.class)
    public void forgotPasswordRequest(String email) {
        // 1. Buscar al usuario por su email
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        // 2. Por seguridad, si el usuario no existe, no hacemos nada y no informamos del error.
        // Esto evita que atacantes adivinen qué correos están registrados.
        if (userOptional.isEmpty()) {
            System.out.println("Intento de reseteo para email no existente: " + email);
            return;
        }

        User user = userOptional.get();

        // Invalidamos cualquier token de sesión activo
        List<Token> activeTokens = tokenRepository.findByUserAndCanActive(user, true);
        activeTokens.forEach(t -> {
            t.setCanActive(false);
            tokenRepository.save(t);
        });

        // Generamos un token que funcionará para resetear la contraseña únicamente
        String tokenString = jwtTools.generateToken(user);
        Token resetToken = new Token(tokenString, user);

        // No necesito que el usuario copie toda la longitud del token porque es muy largo. Con que pueda ingresar 8 valores únicos es suficiente.
        // Tienen que ser los últimos 8 caracteres porque los iniciales son siempre los mismos (depende del id del usuario).
        String shortToken = tokenString.substring(tokenString.length() - 8);

        resetToken.setType(TOKEN_TYPE_PASSWORD_RESET);

        resetToken.setToken(shortToken);
        tokenRepository.save(resetToken);

         notificationFactory.getStrategy(NotificationType.FORGOT_PASSWORD)
                 .sendNotification(resetToken);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void resetPassword(String resetToken, String newPassword) {
        // 1. Buscar el token de reseteo, verificando que sea válido y activo
        Token token = tokenRepository.findByTokenAndTypeAndCanActive(resetToken, TOKEN_TYPE_PASSWORD_RESET, true)
                .orElseThrow(() -> new HttpError(HttpStatus.BAD_REQUEST, "Invalid reset token"));

        // 2. Verificar que el token no haya expirado
        Instant tokenCreationTime = token.getTimestamp().toInstant();
        if (Instant.now().isAfter(tokenCreationTime.plus(RESET_TOKEN_TTL))) {

            // Si el token ya expiró por tiempo pero su campo de canActive sigue en true, lo desactivamos ahora.
            token.setCanActive(false);
            tokenRepository.save(token);

            throw new HttpError(HttpStatus.BAD_REQUEST, "Reset token has expired");
        }

        // 3. Actualizar la contraseña del usuario
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 4. Invalidar el token de reseteo
        token.setCanActive(false);
        tokenRepository.save(token);
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
