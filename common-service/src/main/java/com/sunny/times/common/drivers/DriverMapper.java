package com.sunny.times.common.drivers;

import com.sunny.times.contracts.drivers.DriverDto;

public class DriverMapper {

    public static DriverDto toDto(DriverEntity entity) {
        return new DriverDto(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getLicenseNumber(),
                entity.getAssignedVehicleId()
        );
    }
}
