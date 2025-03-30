package com.distribuidor.reparto.listener;

import com.distribuidor.reparto.modelo.Usuario;
import com.distribuidor.reparto.service.Auth.AuthServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private static final Logger log = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);

    @Autowired
    private AuthServiceImpl userService;

    @Autowired
    private JavaMailSender mailSender;

    private Usuario theUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event){

        theUser= event.getUser();

        String verificationToken= UUID.randomUUID().toString();

        userService.saveUserVerificationToken(theUser, verificationToken);


        String url= "http://localhost:8080/api/auth/verifyEmail?token="+verificationToken;

        try{
            sendVerificationEmail(url);
        }catch (MessagingException | UnsupportedEncodingException e){

            throw new RuntimeException(e);
        }
        log.info("Haga click en el enlace para verificar su registro: { }", url);

    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject= "Verificacion de correo electronico";
        String senderName="Servicio de registro de usuarios";
        String mailContent="<p> Hola, "+ this.theUser.getUsername() + "</p>"
                + " <p> Gracias por registrarte con nosotros</p>"
                + "<p> Por favor haz click en el enlace para completar tu registro: </p>"
                + "<a href=\""+ url + "\"> Verifica tu correo electronico para activar tu cuenta </a>"
                + "<p> Gracias, <br> Servicio de Registro de Usuarios</p>";

        MimeMessage message= mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("jonathanleder.1996@gmail.com",senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        mailSender.send(message);
    }


}
