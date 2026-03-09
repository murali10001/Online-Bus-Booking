package com.busticket.controller;

import com.busticket.model.Passenger;
import com.busticket.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    // POST /api/passengers - Register passenger
    @PostMapping
    public ResponseEntity<Passenger> registerPassenger(@Valid @RequestBody Passenger passenger) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.registerPassenger(passenger));
    }

    // GET /api/passengers - Get all passengers
    @GetMapping
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    // GET /api/passengers/{id} - Get passenger by ID
    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    // GET /api/passengers/email/{email} - Get passenger by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Passenger> getPassengerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(passengerService.getPassengerByEmail(email));
    }

    // PUT /api/passengers/{id} - Update passenger
    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id,
                                                      @Valid @RequestBody Passenger passenger) {
        return ResponseEntity.ok(passengerService.updatePassenger(id, passenger));
    }

    // DELETE /api/passengers/{id} - Delete passenger
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePassenger(@PathVariable Long id) {
        String message = passengerService.deletePassenger(id);
        return ResponseEntity.ok(Map.of("message", message));
    }
}