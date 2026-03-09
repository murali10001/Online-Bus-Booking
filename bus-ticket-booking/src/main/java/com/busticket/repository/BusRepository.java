package com.busticket.repository;

import com.busticket.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    Optional<Bus> findByBusNumber(String busNumber);

    // Search by source and destination (case-insensitive)
    List<Bus> findBySourceIgnoreCaseAndDestinationIgnoreCaseAndStatus(
            String source, String destination, String status);

    // All active buses
    List<Bus> findByStatus(String status);

    // Buses with available seats on a route
    @Query("SELECT b FROM Bus b WHERE LOWER(b.source) = LOWER(:source) " +
           "AND LOWER(b.destination) = LOWER(:destination) " +
           "AND b.availableSeats >= :seats AND b.status = 'ACTIVE'")
    List<Bus> findAvailableBuses(String source, String destination, int seats);

    boolean existsByBusNumber(String busNumber);
}
