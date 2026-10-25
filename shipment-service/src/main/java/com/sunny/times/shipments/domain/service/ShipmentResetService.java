package com.sunny.times.shipments.domain.service;

import com.sunny.times.shipments.persistence.repository.ShipmentRepository;
import com.sunny.times.shipments.persistence.repository.ShipmentStatusHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipmentResetService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentStatusHistoryRepository shipmentStatusHistoryRepository;

    public ShipmentResetService(
            ShipmentRepository shipmentRepository,
            ShipmentStatusHistoryRepository shipmentStatusHistoryRepository
    ) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentStatusHistoryRepository = shipmentStatusHistoryRepository;
    }

    public void reset() {

        shipmentStatusHistoryRepository.deleteAll();

        shipmentRepository.deleteAll();
    }
}