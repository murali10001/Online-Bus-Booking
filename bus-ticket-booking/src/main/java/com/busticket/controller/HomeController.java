package com.busticket.controller;

import com.busticket.model.Booking;
import com.busticket.repository.BookingRepository;
import com.busticket.repository.BusRepository;
import com.busticket.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // GET / — Dashboard
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalBuses",        busRepository.count());
        model.addAttribute("activeBuses",       busRepository.findByStatus("ACTIVE").size());
        model.addAttribute("totalPassengers",   passengerRepository.count());
        model.addAttribute("confirmedBookings", bookingRepository.findByBookingStatus("CONFIRMED").size());
        model.addAttribute("cancelledBookings", bookingRepository.findByBookingStatus("CANCELLED").size());

        // Recent 10 bookings
        List<Booking> all = bookingRepository.findAll();
        int fromIdx = Math.max(0, all.size() - 10);
        model.addAttribute("recentBookings", all.subList(fromIdx, all.size()));

        return "index";
    }

    // GET /search — Search page with @RequestParam
    @GetMapping("/search")
    public String searchPage(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false, defaultValue = "1") int seats,
            Model model) {

        model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("seats", seats);

        if (source != null && destination != null && !source.isBlank() && !destination.isBlank()) {
            model.addAttribute("searched", true);
            model.addAttribute("buses",
                    busRepository.findAvailableBuses(source, destination, seats));
        } else {
            model.addAttribute("searched", false);
        }

        return "search";
    }
}