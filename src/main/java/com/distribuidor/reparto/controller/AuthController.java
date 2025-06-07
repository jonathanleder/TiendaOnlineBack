package com.distribuidor.reparto.controller;

import com.distribuidor.reparto.service.Auth.AuthService;
import com.distribuidor.reparto.service.Auth.AuthServiceImpl;
import com.distribuidor.reparto.token.VerificationToken;
import com.distribuidor.reparto.token.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthServiceImpl userService;

    @Autowired
    private VerificationTokenRepository tokenRepository;

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
            if (e.getMessage().contains("Usuario no encontrado")) {
                errorMessage = "Usuario no encontrado";
            } else if (e.getMessage().contains("La cuenta no ha sido verificada")) {
                errorMessage = "La cuenta no ha sido verificada. por favor revise su correo electronico";
            }else if(e.getMessage().contains("Bad credentials")) {
                errorMessage = "Usuario o contraseña incorrectos";
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
            var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.USER_CREATE_SUCCESSFULLY,"Usuario creado con exito, por favor revise su correo electronico para completar el registro");
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

    @GetMapping("verifyEmail")
    public void VerifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {

        System.out.println("Recibida la solicitud para verificacion: "+token);

        try{
            VerificationToken verificationToken = tokenRepository.findByToken(token);
            if(verificationToken == null) {
                System.out.println("Token no encontrado");
                response.sendRedirect("http://localhost:5173/verification?status=invalid-token");
                return;
            }
            String result =userService.validateToken(token);
            System.out.println("Resultado: "+result);

            if (!"valido".equals(result) && !"expired".equals(result)) {
                response.sendRedirect("http://localhost:5173/verification?status=error");
                return;
            }
            if ("expired".equals(result)) {
                response.sendRedirect("http://localhost:5173/verification?status=expired");
                return;
            }
            response.sendRedirect("http://localhost:5173/verification?status=success");

        }catch (Exception e){
            System.out.println("Error durante la verificacion: "+e.getMessage());
            e.printStackTrace();
            response.sendRedirect("http://localhost:5173/verification?status=error");
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
