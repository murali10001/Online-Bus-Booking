package com.busticket.controller;

import com.busticket.model.Bus;
import com.busticket.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping
    public ResponseEntity<Bus> addBus(@Valid @RequestBody Bus bus) {
        return ResponseEntity.status(HttpStatus.CREATED).body(busService.addBus(bus));
    }

    @GetMapping
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Bus>> getActiveBuses() {
        return ResponseEntity.ok(busService.getActiveBuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getBusById(id));
    }

    @GetMapping("/number/{busNumber}")
    public ResponseEntity<Bus> getBusByNumber(@PathVariable String busNumber) {
        return ResponseEntity.ok(busService.getBusByNumber(busNumber));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Bus>> searchBuses(
            @RequestParam String source,
            @RequestParam String destination) {
        return ResponseEntity.ok(busService.searchBuses(source, destination));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Bus>> searchAvailableBuses(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(defaultValue = "1") int seats) {
        return ResponseEntity.ok(busService.searchAvailableBuses(source, destination, seats));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> checkSeatAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(busService.checkSeatAvailability(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @Valid @RequestBody Bus bus) {
        return ResponseEntity.ok(busService.updateBus(id, bus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBus(@PathVariable Long id) {
        String message = busService.deleteBus(id);
        return ResponseEntity.ok(Map.of("message", message));
    }
}