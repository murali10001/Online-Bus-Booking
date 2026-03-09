package com.busticket.repository;

import com.busticket.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
