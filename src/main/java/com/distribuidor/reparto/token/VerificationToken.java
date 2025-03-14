package com.distribuidor.reparto.token;


import com.distribuidor.reparto.modelo.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;

    @Column(nullable= false)
    private LocalDateTime expirationTime;

    private static final int EXPIRATION_TIME = 15;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Usuario user;

    public VerificationToken() {

    }
    public VerificationToken(String token, Usuario user) {
        this.token = token;
        this.expirationTime = calculateExpirationTime();
        this.user = user;
    }

    private LocalDateTime calculateExpirationTime() {
        return LocalDateTime.now().plus(EXPIRATION_TIME, ChronoUnit.MINUTES);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
