package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
   Optional<Shipment> findShipmentByOrder_Id(Long orderId);
}
