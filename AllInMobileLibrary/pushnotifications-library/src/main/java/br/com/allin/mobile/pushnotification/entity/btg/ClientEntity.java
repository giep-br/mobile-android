package br.com.allin.mobile.pushnotification.entity.btg;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class ClientEntity {
    private String email;

    public ClientEntity(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
