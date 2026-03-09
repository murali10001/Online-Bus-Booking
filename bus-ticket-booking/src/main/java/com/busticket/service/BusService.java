package com.busticket.service;

import com.busticket.exception.BusinessException;
import com.busticket.exception.ResourceNotFoundException;
import com.busticket.model.Bus;
import com.busticket.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BusService {

    @Autowired
    private BusRepository busRepository;

    // ========== ADD BUS ==========
    public Bus addBus(Bus bus) {
        if (busRepository.existsByBusNumber(bus.getBusNumber())) {
            throw new BusinessException("Bus with number '" + bus.getBusNumber() + "' already exists.");
        }
        bus.setAvailableSeats(bus.getTotalSeats());
        bus.setStatus("ACTIVE");
        return busRepository.save(bus);
    }

    // ========== GET ALL BUSES ==========
    @Transactional(readOnly = true)
    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    // ========== GET ACTIVE BUSES ==========
    @Transactional(readOnly = true)
    public List<Bus> getActiveBuses() {
        return busRepository.findByStatus("ACTIVE");
    }

    // ========== GET BUS BY ID ==========
    @Transactional(readOnly = true)
    public Bus getBusById(Long busId) {
        return busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with ID: " + busId));
    }

    // ========== GET BUS BY NUMBER ==========
    @Transactional(readOnly = true)
    public Bus getBusByNumber(String busNumber) {
        return busRepository.findByBusNumber(busNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with number: " + busNumber));
    }

    // ========== SEARCH BUSES ==========
    @Transactional(readOnly = true)
    public List<Bus> searchBuses(String source, String destination) {
        return busRepository.findBySourceIgnoreCaseAndDestinationIgnoreCaseAndStatus(
                source, destination, "ACTIVE");
    }

    // ========== SEARCH AVAILABLE BUSES ==========
    @Transactional(readOnly = true)
    public List<Bus> searchAvailableBuses(String source, String destination, int seats) {
        return busRepository.findAvailableBuses(source, destination, seats);
    }

    // ========== UPDATE BUS ==========
    public Bus updateBus(Long busId, Bus updatedBus) {
        Bus existing = getBusById(busId);

        if (!existing.getBusNumber().equals(updatedBus.getBusNumber())
                && busRepository.existsByBusNumber(updatedBus.getBusNumber())) {
            throw new BusinessException("Bus number '" + updatedBus.getBusNumber() + "' is already taken.");
        }

        int bookedSeats = existing.getTotalSeats() - existing.getAvailableSeats();
        if (updatedBus.getTotalSeats() < bookedSeats) {
            throw new BusinessException("Cannot reduce total seats below already booked seats (" + bookedSeats + ").");
        }

        existing.setBusName(updatedBus.getBusName());
        existing.setBusNumber(updatedBus.getBusNumber());
        existing.setSource(updatedBus.getSource());
        existing.setDestination(updatedBus.getDestination());
        existing.setTotalSeats(updatedBus.getTotalSeats());
        existing.setAvailableSeats(updatedBus.getTotalSeats() - bookedSeats);
        existing.setTicketPrice(updatedBus.getTicketPrice());
        existing.setDepartureTime(updatedBus.getDepartureTime());
        existing.setArrivalTime(updatedBus.getArrivalTime());

        return busRepository.save(existing);
    }

    // ========== DELETE / DEACTIVATE BUS ==========
    public String deleteBus(Long busId) {
        Bus bus = getBusById(busId);
        bus.setStatus("INACTIVE");
        busRepository.save(bus);
        return "Bus '" + bus.getBusName() + "' has been deactivated successfully.";
    }

    // ========== CHECK SEAT AVAILABILITY ==========
    @Transactional(readOnly = true)
    public Map<String, Object> checkSeatAvailability(Long busId) {
        Bus bus = getBusById(busId);
        Map<String, Object> result = new HashMap<>();
        result.put("busId", bus.getBusId());
        result.put("busName", bus.getBusName());
        result.put("busNumber", bus.getBusNumber());
        result.put("source", bus.getSource());
        result.put("destination", bus.getDestination());
        result.put("totalSeats", bus.getTotalSeats());
        result.put("availableSeats", bus.getAvailableSeats());
        result.put("bookedSeats", bus.getTotalSeats() - bus.getAvailableSeats());
        result.put("ticketPrice", bus.getTicketPrice());
        result.put("departureTime", bus.getDepartureTime());
        result.put("arrivalTime", bus.getArrivalTime());
        result.put("status", bus.getStatus());
        return result;
    }
}