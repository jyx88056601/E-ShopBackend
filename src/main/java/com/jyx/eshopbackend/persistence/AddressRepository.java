package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    // find all the address under user's id
    Optional<List<Address>> findAddressesByUser_Id(Long userId);


}
