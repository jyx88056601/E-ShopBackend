package com.jyx.eshopbackend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jyx.eshopbackend.dto.ShipmentResponseDTO;
import com.jyx.eshopbackend.model.OrderStatus;
import com.jyx.eshopbackend.model.Shipment;
import com.jyx.eshopbackend.model.ShipmentStatus;
import com.jyx.eshopbackend.persistence.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;

    private final OrderService orderService;

    private final AddressService addressService;

    public ShipmentService(ShipmentRepository shipmentRepository, OrderService orderService, AddressService addressService) {
        this.shipmentRepository = shipmentRepository;
        this.orderService = orderService;
        this.addressService = addressService;
    }

    @Transactional
    public Optional<ShipmentResponseDTO> InitializeShipment(Long addressId, String orderId) {
        var order = orderService.findOrderById(orderId).orElseThrow(() -> new NotFoundException("No order found with order id = " + orderId));
        if(order.getShipment() != null) {
            throw new RuntimeException("Shipment is processing");
        }
        var address = addressService.findOrderById(addressId).orElseThrow(() -> new NotFoundException("No address found with address id with " +addressId));
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setAddress(address);
        var storedShipment = shipmentRepository.save(shipment);
        order.setShipment(storedShipment);
        order.setOrderStatus(OrderStatus.PROCESSING);
        orderService.updateOrder(order);
        return  Optional.of(new ShipmentResponseDTO(storedShipment));
    }

}
