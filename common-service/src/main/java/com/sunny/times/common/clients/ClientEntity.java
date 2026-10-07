package com.sunny.times.common.clients;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_profiles")
public class ClientEntity {

    @Id
    private Long userId;

    private String companyName;

    private String companyAddress;

    private String vatNumber;

    public ClientEntity() {
    }

    public ClientEntity(
            Long userId,
            String companyName,
            String companyAddress,
            String vatNumber
    ) {
        this.userId = userId;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.vatNumber = vatNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getVatNumber() {
        return vatNumber;
    }
}
