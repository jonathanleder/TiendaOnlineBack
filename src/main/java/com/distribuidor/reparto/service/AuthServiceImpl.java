package com.distribuidor.reparto.service;

import com.distribuidor.reparto.modelo.Usuario;
import com.distribuidor.reparto.repository.UserRepo;
import com.distribuidor.reparto.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Override
    public String login(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        var authenticate= authenticationManager.authenticate(authenticationToken);
        return JwtUtils.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername());
    }

    @Override
    public String singUp(String nombre,String apellido,String telefono, String email, String username, String password) {

        if(userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username ya existe");
        }
        if(userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email ya existe");
        }
        Usuario user = new Usuario(nombre,apellido,telefono,username,passwordEncoder.encode(password),email);


        userRepo.save(user);
        System.out.println("Usuario Creado "+ user.getUsername());
        return JwtUtils.generateToken(user.getUsername());
    }

    @Override
    public String verifyToken(String token) {

        var username = JwtUtils.getUsernameFromToken(token);

        if(username.isPresent()) {
            return username.get();
        }
        throw new RuntimeException("Token invalido");



    }
}
