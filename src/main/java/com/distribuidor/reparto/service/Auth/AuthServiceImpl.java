package com.distribuidor.reparto.service.Auth;

import com.distribuidor.reparto.listener.RegistrationCompleteEvent;
import com.distribuidor.reparto.modelo.Usuario;
import com.distribuidor.reparto.repository.UserRepo;
import com.distribuidor.reparto.token.VerificationToken;
import com.distribuidor.reparto.token.VerificationTokenRepository;
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

        Optional<Usuario> usuario = userRepo.findByUsername(username);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        Usuario usuarioActual = usuario.get();

        if(!usuarioActual.isEnabled()){
            throw new RuntimeException("La cuenta no ah sido verificada");
        }


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
    public String verifyToken(String token) {

        var username = JwtUtils.getUsernameFromToken(token);

        if(username.isPresent()) {
            return username.get();
        }
        throw new RuntimeException("Token invalido");



    }

    @Override
    public Optional<Usuario> findByEmail(String email){
        return userRepo.findByEmail(email);
    }


    @Override
    public void saveUserVerificationToken(Usuario theUser, String token) {

        verificationTokenRepository.save(new VerificationToken(token,theUser));

    }

    @Override
    public String validateToken(String token) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null) {
            System.out.println("Token no encontrado en la bd");
            return "Token de verificacion no valido";
        }
        Usuario user = verificationToken.getUser();
        System.out.println("Estado actual del usuario: "+ user.isEnabled());
        if(verificationToken.isExpired()){
            verificationTokenRepository.delete(verificationToken);
            System.out.println("Token expirado y eliminado");
            return "expired";
        }
        user.setEnabled(true);
        try {
            userRepo.save(user);
            System.out.println("Usuario Actualizado :" + user.isEnabled());
            verificationTokenRepository.delete(verificationToken);
            return "valido";
        }catch (Exception e){
            System.out.println("Error al guardar usuario: "+ e.getMessage());
            e.printStackTrace();
            return "Error al actualizar usuario";
        }

    }
}
