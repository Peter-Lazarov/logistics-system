package com.sunny.times.common.drivers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "drivers")
public class DriverEntity {

    @Id
    private String id;

    private String name;
    private String phone;
    private String licenseNumber;
    private String assignedVehicleId;

    public DriverEntity() {}

    public DriverEntity(String id, String name, String phone, String licenseNumber, String assignedVehicleId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.assignedVehicleId = assignedVehicleId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getAssignedVehicleId() {
        return assignedVehicleId;
    }
}
