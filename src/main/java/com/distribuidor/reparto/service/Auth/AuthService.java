package com.distribuidor.reparto.service.Auth;

import com.distribuidor.reparto.modelo.Usuario;

import java.util.Optional;

public interface AuthService {

    String login(String username, String password);

    String singUp(String nombre,String apellido,String telefono,String email,String username, String password);

    String verifyToken(String token);

    Optional<Usuario> findByEmail(String email);

    void saveUserVerificationToken(Usuario theUser,String verificationToken);

    String validateToken(String token);
}
