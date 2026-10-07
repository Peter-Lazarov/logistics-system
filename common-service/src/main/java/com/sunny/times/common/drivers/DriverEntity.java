package com.sunny.times.common.drivers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "driver_profiles")
public class DriverEntity {

    @Id
    private Long userId;

    private String licenseNumber;

    private String assignedVehicleId;

    public DriverEntity() {
    }

    public DriverEntity(
            Long userId,
            String licenseNumber,
            String assignedVehicleId
    ) {
        this.userId = userId;
        this.licenseNumber = licenseNumber;
        this.assignedVehicleId = assignedVehicleId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getAssignedVehicleId() {
        return assignedVehicleId;
    }
}
