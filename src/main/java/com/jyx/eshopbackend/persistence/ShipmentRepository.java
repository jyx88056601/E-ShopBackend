package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
   Optional<Shipment> findShipmentByOrder_Id(UUID orderId);
}
