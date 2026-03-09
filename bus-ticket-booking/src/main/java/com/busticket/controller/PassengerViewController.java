package com.busticket.controller;

import com.busticket.model.Passenger;
import com.busticket.service.BookingService;
import com.busticket.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/passengers")
@RequiredArgsConstructor
public class PassengerViewController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private BookingService bookingService;

    // GET /passengers
    @GetMapping
    public String listPassengers(Model model) {
        model.addAttribute("passengers", passengerService.getAllPassengers());
        return "passengers/list";
    }

    // GET /passengers/new
    @GetMapping("/new")
    public String newPassengerForm(Model model) {
        model.addAttribute("passenger", new Passenger());
        return "passengers/form";
    }

    // GET /passengers/{id}
    @GetMapping("/{id}")
    public String viewPassenger(@PathVariable Long id, Model model) {
        model.addAttribute("passenger", passengerService.getPassengerById(id));
        model.addAttribute("bookings", bookingService.getBookingsByPassenger(id));
        return "passengers/detail";
    }

    // GET /passengers/{id}/edit
    @GetMapping("/{id}/edit")
    public String editPassengerForm(@PathVariable Long id, Model model) {
        model.addAttribute("passenger", passengerService.getPassengerById(id));
        return "passengers/form";
    }

    // POST /passengers/add — binds form fields using @ModelAttribute
    @PostMapping("/add")
    public String addPassenger(@ModelAttribute Passenger passenger, RedirectAttributes ra) {
        try {
            Passenger saved = passengerService.registerPassenger(passenger);
            ra.addFlashAttribute("message", "Passenger '" + saved.getPassengerName() + "' registered!");
            return "redirect:/passengers";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/passengers/new";
        }
    }

    // POST /passengers/{id}/update — @ModelAttribute binds form fields
    @PostMapping("/{id}/update")
    public String updatePassenger(@PathVariable Long id,
                                  @ModelAttribute Passenger passenger,
                                  RedirectAttributes ra) {
        try {
            passengerService.updatePassenger(id, passenger);
            ra.addFlashAttribute("message", "Passenger updated.");
            return "redirect:/passengers";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/passengers/" + id + "/edit";
        }
    }

    // POST /passengers/{id}/delete
    @PostMapping("/{id}/delete")
    public String deletePassenger(@PathVariable Long id, RedirectAttributes ra) {
        try {
            String msg = passengerService.deletePassenger(id);
            ra.addFlashAttribute("message", msg);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/passengers";
    }
}