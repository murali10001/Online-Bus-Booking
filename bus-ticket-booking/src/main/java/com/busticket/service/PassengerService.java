package com.busticket.service;

import com.busticket.exception.BusinessException;
import com.busticket.exception.ResourceNotFoundException;
import com.busticket.model.Passenger;
import com.busticket.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    // ========== REGISTER PASSENGER ==========
    public Passenger registerPassenger(Passenger passenger) {
        if (passengerRepository.existsByEmail(passenger.getEmail())) {
            throw new BusinessException("Email '" + passenger.getEmail() + "' is already registered.");
        }
        return passengerRepository.save(passenger);
    }

    // ========== GET ALL PASSENGERS ==========
    @Transactional(readOnly = true)
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    // ========== GET PASSENGER BY ID ==========
    @Transactional(readOnly = true)
    public Passenger getPassengerById(Long passengerId) {
        return passengerRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with ID: " + passengerId));
    }

    // ========== GET PASSENGER BY EMAIL ==========
    @Transactional(readOnly = true)
    public Passenger getPassengerByEmail(String email) {
        return passengerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with email: " + email));
    }

    // ========== UPDATE PASSENGER ==========
    public Passenger updatePassenger(Long passengerId, Passenger updatedPassenger) {
        Passenger existing = getPassengerById(passengerId);

        if (!existing.getEmail().equals(updatedPassenger.getEmail())
                && passengerRepository.existsByEmail(updatedPassenger.getEmail())) {
            throw new BusinessException("Email '" + updatedPassenger.getEmail() + "' is already taken.");
        }

        existing.setPassengerName(updatedPassenger.getPassengerName());
        existing.setEmail(updatedPassenger.getEmail());
        existing.setPhoneNumber(updatedPassenger.getPhoneNumber());
        existing.setAddress(updatedPassenger.getAddress());
        existing.setAge(updatedPassenger.getAge());
        existing.setGender(updatedPassenger.getGender());

        return passengerRepository.save(existing);
    }

    // ========== DELETE PASSENGER ==========
    public String deletePassenger(Long passengerId) {
        Passenger passenger = getPassengerById(passengerId);
        passengerRepository.delete(passenger);
        return "Passenger '" + passenger.getPassengerName() + "' deleted successfully.";
    }
}