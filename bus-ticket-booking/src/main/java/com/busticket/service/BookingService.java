package com.busticket.service;

import com.busticket.exception.BusinessException;
import com.busticket.exception.ResourceNotFoundException;
import com.busticket.model.Booking;
import com.busticket.model.Bus;
import com.busticket.model.Passenger;
import com.busticket.repository.BookingRepository;
import com.busticket.repository.BusRepository;
import com.busticket.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    // ========== BOOK TICKET ==========
    public Booking bookTicket(Long busId, Long passengerId, int numberOfSeats, String journeyDate) {

        // Fetch Bus
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with ID: " + busId));

        // Fetch Passenger
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with ID: " + passengerId));

        // Validate bus is active
        if (!"ACTIVE".equalsIgnoreCase(bus.getStatus())) {
            throw new BusinessException("Bus '" + bus.getBusName() + "' is not active.");
        }

        // Check seat availability
        if (bus.getAvailableSeats() < numberOfSeats) {
            throw new BusinessException("Not enough seats available. Available: "
                    + bus.getAvailableSeats() + ", Requested: " + numberOfSeats);
        }

        // Calculate fare
        double totalFare = bus.getTicketPrice() * numberOfSeats;

        // Deduct seats
        bus.setAvailableSeats(bus.getAvailableSeats() - numberOfSeats);
        busRepository.save(bus);

        // Create booking
        Booking booking = new Booking();
        booking.setBus(bus);
        booking.setPassenger(passenger);
        booking.setNumberOfSeats(numberOfSeats);
        booking.setTotalFare(totalFare);
        booking.setBookingStatus("CONFIRMED");
        booking.setBookingDate(LocalDateTime.now());
        booking.setJourneyDate(journeyDate);

        return bookingRepository.save(booking);
    }

    // ========== CANCEL TICKET ==========
    public Booking cancelTicket(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if ("CANCELLED".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new BusinessException("Booking is already cancelled. PNR: " + booking.getPnrNumber());
        }

        // Restore seats
        Bus bus = booking.getBus();
        bus.setAvailableSeats(bus.getAvailableSeats() + booking.getNumberOfSeats());
        busRepository.save(bus);

        // Update booking status
        booking.setBookingStatus("CANCELLED");
        return bookingRepository.save(booking);
    }

    // ========== CANCEL BY PNR ==========
    public Booking cancelTicketByPnr(String pnrNumber) {
        Booking booking = bookingRepository.findByPnrNumber(pnrNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with PNR: " + pnrNumber));
        return cancelTicket(booking.getBookingId());
    }

    // ========== GET BOOKING BY ID ==========
    @Transactional(readOnly = true)
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
    }

    // ========== GET BOOKING BY PNR ==========
    @Transactional(readOnly = true)
    public Booking getBookingByPnr(String pnrNumber) {
        return bookingRepository.findByPnrNumber(pnrNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with PNR: " + pnrNumber));
    }

    // ========== GET ALL BOOKINGS ==========
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // ========== GET BOOKINGS BY PASSENGER ==========
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByPassenger(Long passengerId) {
        // Validate passenger exists
        passengerRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with ID: " + passengerId));
        return bookingRepository.findByPassenger_PassengerId(passengerId);
    }

    // ========== GET BOOKINGS BY BUS ==========
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByBus(Long busId) {
        busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with ID: " + busId));
        return bookingRepository.findByBus_BusId(busId);
    }

    // ========== GET CONFIRMED BOOKINGS BY PASSENGER ==========
    @Transactional(readOnly = true)
    public List<Booking> getConfirmedBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassenger_PassengerIdAndBookingStatus(passengerId, "CONFIRMED");
    }
}