package com.busticket.repository;

import com.busticket.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByPnrNumber(String pnrNumber);

    List<Booking> findByPassenger_PassengerId(Long passengerId);

    List<Booking> findByBus_BusId(Long busId);

    List<Booking> findByBookingStatus(String status);

    List<Booking> findByPassenger_PassengerIdAndBookingStatus(Long passengerId, String status);
}
