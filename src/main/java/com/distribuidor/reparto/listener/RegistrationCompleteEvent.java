package com.distribuidor.reparto.listener;

import com.distribuidor.reparto.modelo.Usuario;
import org.springframework.context.ApplicationEvent;

public class RegistrationCompleteEvent  extends ApplicationEvent {
    private Usuario user;
    private String applicationUrl;

    public RegistrationCompleteEvent(Usuario user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

    public Usuario getUser() {
        return this.user;
    }
    public String getApplicationUrl() {
        return this.applicationUrl;
    }
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }
    public void setUser(Usuario user) {
        this.user = user;
    }
}
