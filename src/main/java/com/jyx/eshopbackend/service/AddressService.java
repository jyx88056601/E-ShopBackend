package com.jyx.eshopbackend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jyx.eshopbackend.dto.AddressListDTO;
import com.jyx.eshopbackend.dto.CreatingAddressRequestDTO;
import com.jyx.eshopbackend.model.Address;
import com.jyx.eshopbackend.persistence.AddressRepository;
import com.jyx.eshopbackend.persistence.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    private final UserService userService;
    private final OrderRepository orderRepository;

    public AddressService(AddressRepository addressRepository, UserService userService,
                          OrderRepository orderRepository) {
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public String storeNewAddress(CreatingAddressRequestDTO addressDTO, Long userId) {
        try {
            var user = userService.findUserById(userId)
                    .orElseThrow(() -> new NotFoundException("corresponding user with userid " + userId + " not found"));

            Address newAddress = new Address();
            newAddress.setRecipientName(sanitizeString(addressDTO.getFirstName()) + " " + sanitizeString(addressDTO.getLastName()));
            newAddress.setCountry(sanitizeString(addressDTO.getCountry()));
            newAddress.setProvince(sanitizeString(addressDTO.getProvince()));
            newAddress.setCity(sanitizeString(addressDTO.getCity()));
            newAddress.setHouseNumber(sanitizeString(addressDTO.getHouseNumber()));
            newAddress.setUnitNumber(sanitizeString(addressDTO.getUnitNumber()));
            newAddress.setPostalCode(sanitizeString(addressDTO.getPostalCode()));
            newAddress.setStreetName(sanitizeString(addressDTO.getStreetName()));
            newAddress.setBuildingNumber(sanitizeString(addressDTO.getBuildingNumber()));
            newAddress.setCommunity(sanitizeString(addressDTO.getCommunity()));
            newAddress.setDistrict(sanitizeString(addressDTO.getDistrict()));
            newAddress.setPhoneNumber(user.getPhoneNumber());
            var storedAddress = addressRepository.save(newAddress);

            if (user.getAddressIds() == null) {
                user.setAddressIds(new ArrayList<>());
            }

            user.getAddressIds().add(storedAddress.getId());
            userService.updateUser(user);

            return "Success";
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String sanitizeString(String str) {
        return str == null || str.trim().isEmpty() ? "" : str.trim();
    }

    @Transactional
    public AddressListDTO fetchAddress(Long userId) {
        try {
            List<String> addressList = new ArrayList<>();
            var user = userService.findUserById(userId)
                    .orElseThrow(() -> new NotFoundException("corresponding user with userid " + userId + " not found"));
            var ids = user.getAddressIds();
            for (Long id : ids) {
                var address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("No address found with id = " + id));
                addressList.add(address.toString());
            }
            return new AddressListDTO(addressList);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage());
        }
    }

    public Optional<Address> findOrderById(Long addressId) {
        return addressRepository.findById(addressId);
    }



}
