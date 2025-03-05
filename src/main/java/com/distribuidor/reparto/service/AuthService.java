package com.distribuidor.reparto.service;

public interface AuthService {

    String login(String username, String password);

    String singUp(String nombre,String apellido,String telefono,String email,String username, String password);

    String verifyToken(String token);
}
