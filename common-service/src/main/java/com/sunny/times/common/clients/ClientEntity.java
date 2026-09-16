package com.sunny.times.common.clients;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    private String clientId;

    private String name;
    private String phone;
    private String email;

    public ClientEntity() {}

    public ClientEntity(String clientId, String name, String phone, String email) {
        this.clientId = clientId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
