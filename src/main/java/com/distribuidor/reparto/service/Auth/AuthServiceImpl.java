package com.distribuidor.reparto.service.Auth;

import com.distribuidor.reparto.modelo.Usuario;
import com.distribuidor.reparto.repository.UserRepo;
import com.distribuidor.reparto.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public String login(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        var authenticate= authenticationManager.authenticate(authenticationToken);
        return JwtUtils.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername());
    }

    @Override
    public String singUp(String nombre,String apellido,String telefono, String email, String username, String password) {

        if(userRepo.existsByUsername(username)) {
            throw new RuntimeException("El username ya existe");
        }
        if(userRepo.existsByEmail(email)) {
            throw new RuntimeException("El email ya existe");
        }
        Usuario user = new Usuario(nombre,apellido,telefono,username,passwordEncoder.encode(password),email);
        user.setEnabled(false);

        userRepo.save(user);

        eventPublisher.publishEvent(new RegistrationCompleteEvent(user,"..."));


        System.out.println("Usuario Creado "+ user.getUsername());
        return "Verifica tu email";
    }

    @Override
    Optional<Usuario> findByEmail(String email){
        return userRepo.findByEmail(email);
    }

    @Override
    public String verifyToken(String token) {

        var username = JwtUtils.getUsernameFromToken(token);

        if(username.isPresent()) {
            return username.get();
        }
        throw new RuntimeException("Token invalido");



    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void saveUserVerificationToken(Usuario theUser, String verificationToken) {

    }

    @Override
    public String validateToken(String token) {
        return "";
    }
}
