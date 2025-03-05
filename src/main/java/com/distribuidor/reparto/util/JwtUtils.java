package com.distribuidor.reparto.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class JwtUtils {
    private static final SecretKey secretKey= Jwts.SIG.HS256.key().build();
    private static final String ISSUER = "server";

    private JwtUtils(){
    }

    public static boolean validateToken(String jwtToken){
        return parseToken(jwtToken).isPresent();
    }

    public static Optional<Claims> parseToken(String jwtToken) {
        var jwtParser = Jwts.parser().verifyWith(secretKey)
                .build();

        try{
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            System.err.print("JWT Exception ocurred: "+ e.getMessage());

        }
        return Optional.empty();
    }

    public static Optional<String> getUsernameFromToken(String jwtToken) {
        var claimsOptional= parseToken(jwtToken);

        return claimsOptional.map(Claims::getSubject);
    }

    public static String generateToken(String username){
        var currentDate =  new Date();
        var jwtExpirationInMinutes = 10;
        var expiration = DateUtils.addMinutes(new Date(), jwtExpirationInMinutes);

        return Jwts.builder().id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();
    }
}
