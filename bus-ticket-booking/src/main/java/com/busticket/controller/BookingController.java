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

    
    @PostMapping
    public ResponseEntity<Booking> bookTicket(@RequestBody Map<String, Object> request) {
        Long busId = Long.valueOf(request.get("busId").toString());
        Long passengerId = Long.valueOf(request.get("passengerId").toString());
        int numberOfSeats = Integer.parseInt(request.get("numberOfSeats").toString());
        String journeyDate = request.get("journeyDate").toString();

        Booking booking = bookingService.bookTicket(busId, passengerId, numberOfSeats, journeyDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/pnr/{pnrNumber}")
    public ResponseEntity<Booking> getBookingByPnr(@PathVariable String pnrNumber) {
        return ResponseEntity.ok(bookingService.getBookingByPnr(pnrNumber));
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<Booking>> getBookingsByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(bookingService.getBookingsByPassenger(passengerId));
    }

    @GetMapping("/passenger/{passengerId}/confirmed")
    public ResponseEntity<List<Booking>> getConfirmedBookings(@PathVariable Long passengerId) {
        return ResponseEntity.ok(bookingService.getConfirmedBookingsByPassenger(passengerId));
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<Booking>> getBookingsByBus(@PathVariable Long busId) {
        return ResponseEntity.ok(bookingService.getBookingsByBus(busId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelTicket(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelTicket(id));
    }

    @PutMapping("/pnr/{pnrNumber}/cancel")
    public ResponseEntity<Booking> cancelTicketByPnr(@PathVariable String pnrNumber) {
        return ResponseEntity.ok(bookingService.cancelTicketByPnr(pnrNumber));
    }
}