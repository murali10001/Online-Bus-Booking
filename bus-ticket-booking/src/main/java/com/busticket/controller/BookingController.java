package com.busticket.controller;

import com.busticket.model.Booking;
import com.busticket.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // POST /api/bookings - Book a ticket
    // Body: { "busId": 1, "passengerId": 1, "numberOfSeats": 2, "journeyDate": "2025-12-25" }
    @PostMapping
    public ResponseEntity<Booking> bookTicket(@RequestBody Map<String, Object> request) {
        Long busId = Long.valueOf(request.get("busId").toString());
        Long passengerId = Long.valueOf(request.get("passengerId").toString());
        int numberOfSeats = Integer.parseInt(request.get("numberOfSeats").toString());
        String journeyDate = request.get("journeyDate").toString();

        Booking booking = bookingService.bookTicket(busId, passengerId, numberOfSeats, journeyDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    // GET /api/bookings - Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // GET /api/bookings/{id} - Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // GET /api/bookings/pnr/{pnrNumber} - Get booking by PNR
    @GetMapping("/pnr/{pnrNumber}")
    public ResponseEntity<Booking> getBookingByPnr(@PathVariable String pnrNumber) {
        return ResponseEntity.ok(bookingService.getBookingByPnr(pnrNumber));
    }

    // GET /api/bookings/passenger/{passengerId} - Get all bookings of a passenger
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<Booking>> getBookingsByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(bookingService.getBookingsByPassenger(passengerId));
    }

    // GET /api/bookings/passenger/{passengerId}/confirmed - Get confirmed bookings of a passenger
    @GetMapping("/passenger/{passengerId}/confirmed")
    public ResponseEntity<List<Booking>> getConfirmedBookings(@PathVariable Long passengerId) {
        return ResponseEntity.ok(bookingService.getConfirmedBookingsByPassenger(passengerId));
    }

    // GET /api/bookings/bus/{busId} - Get all bookings of a bus
    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<Booking>> getBookingsByBus(@PathVariable Long busId) {
        return ResponseEntity.ok(bookingService.getBookingsByBus(busId));
    }

    // PUT /api/bookings/{id}/cancel - Cancel booking by ID
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelTicket(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelTicket(id));
    }

    // PUT /api/bookings/pnr/{pnrNumber}/cancel - Cancel booking by PNR
    @PutMapping("/pnr/{pnrNumber}/cancel")
    public ResponseEntity<Booking> cancelTicketByPnr(@PathVariable String pnrNumber) {
        return ResponseEntity.ok(bookingService.cancelTicketByPnr(pnrNumber));
    }
}