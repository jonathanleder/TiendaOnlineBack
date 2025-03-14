package com.distribuidor.reparto.controller;

import com.distribuidor.reparto.service.Auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {

        try{
        var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
        var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESSFULLY, "Inicio de sesion exitoso");

        return ResponseEntity.status(HttpStatus.OK)
                .body(authResponseDto);

        }catch (Exception e){
            String errorMessage = e.getMessage();
            AuthStatus status = AuthStatus.LOGIN_FAILED;
            if (e.getMessage().contains("Bad credentials")) {
                errorMessage = "Usuario o contraseña incorrectas";
            } else if (e.getMessage().contains("User not found")) {
                errorMessage = "Usuario no encontrado";
            }
            var authResponseDto = new AuthResponseDto(null, status, errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(authResponseDto);
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponseDto> singUP(@RequestBody AuthRequestDto authRequestDto) {
        try {
            var jwtToken = authService.singUp(authRequestDto.nombre(),authRequestDto.apellido(),authRequestDto.telefono() , authRequestDto.email(),authRequestDto.username(), authRequestDto.password());
            var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.USER_CREATE_SUCCESSFULLY,"Usuario registrado exitosamente");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(authResponseDto);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            AuthStatus status = AuthStatus.USER_NOT_CREATE;
            if(e.getMessage().contains("Username already exists")) {
                errorMessage = "El nombre de usuario ya existe";
            } else if (e.getMessage().contains("Email already exist")) {
                errorMessage = "El email ya esta registrado";
            }
            var authResponseDto = new AuthResponseDto(null, status, errorMessage);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(authResponseDto);
        }
    }

}
