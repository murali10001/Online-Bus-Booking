package com.busticket.controller;

import com.busticket.model.Booking;
import com.busticket.service.BookingService;
import com.busticket.service.BusService;
import com.busticket.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingViewController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BusService busService;

    @Autowired
    private PassengerService passengerService;

    // GET /bookings — all bookings list
    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings/list";
    }

    // GET /bookings/new — show booking form
    // Optional @RequestParam busId to pre-select bus
    @GetMapping("/new")
    public String newBookingForm(@RequestParam(required = false) Long busId, Model model) {
        model.addAttribute("buses", busService.getActiveBuses());
        model.addAttribute("passengers", passengerService.getAllPassengers());
        model.addAttribute("preselectedBusId", busId);
        return "bookings/form";
    }

    // POST /bookings/book
    // All parameters from the HTML form via @RequestParam
    @PostMapping("/book")
    public String bookTicket(
            @RequestParam Long busId,
            @RequestParam Long passengerId,
            @RequestParam int numberOfSeats,
            @RequestParam String journeyDate,
            Model model,
            RedirectAttributes ra) {
        try {
            Booking booking = bookingService.bookTicket(busId, passengerId, numberOfSeats, journeyDate);
            return "redirect:/bookings/" + booking.getBookingId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings/new";
        }
    }

    // GET /bookings/{id} — booking detail / confirmation
    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        model.addAttribute("booking", bookingService.getBookingById(id));
        return "bookings/detail";
    }

    // GET /bookings/pnr-search?pnr=XXX — search by PNR via @RequestParam
    @GetMapping("/pnr-search")
    public String searchByPnr(@RequestParam String pnr, Model model, RedirectAttributes ra) {
        try {
            Booking booking = bookingService.getBookingByPnr(pnr);
            return "redirect:/bookings/" + booking.getBookingId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No booking found for PNR: " + pnr);
            return "redirect:/bookings";
        }
    }

    // POST /bookings/{id}/cancel — cancel booking
    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes ra) {
        try {
            bookingService.cancelTicket(id);
            ra.addFlashAttribute("message", "Booking cancelled and seats restored.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/bookings/" + id;
    }

    // POST /bookings/pnr/{pnrNumber}/cancel
    @PostMapping("/pnr/{pnrNumber}/cancel")
    public String cancelByPnr(@PathVariable String pnrNumber, RedirectAttributes ra) {
        try {
            Booking booking = bookingService.cancelTicketByPnr(pnrNumber);
            return "redirect:/bookings/" + booking.getBookingId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings";
        }
    }
}