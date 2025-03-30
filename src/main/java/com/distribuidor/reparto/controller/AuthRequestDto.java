package com.distribuidor.reparto.controller;

public record AuthRequestDto(String nombre,String apellido,String telefono,String email,String username,String password) {
}
