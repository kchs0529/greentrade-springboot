package com.mega.greentrade.repository;
import com.mega.greentrade.entity.Address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByUserid(String userid);
}
