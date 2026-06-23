package com.busticket.controller;

import com.busticket.model.Bus;
import com.busticket.service.BookingService;
import com.busticket.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/buses")
@RequiredArgsConstructor
public class BusViewController {

    @Autowired
    private BusService busService;

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String listBuses(Model model) {
        model.addAttribute("buses", busService.getAllBuses());
        return "buses/list";
    }

    @GetMapping("/new")
    public String newBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        return "buses/form";
    }

    @GetMapping("/{id}")
    public String viewBus(@PathVariable Long id, Model model) {
        model.addAttribute("bus", busService.getBusById(id));
        model.addAttribute("bookings", bookingService.getBookingsByBus(id));
        return "buses/detail";
    }

    @GetMapping("/{id}/edit")
    public String editBusForm(@PathVariable Long id, Model model) {
        model.addAttribute("bus", busService.getBusById(id));
        return "buses/form";
    }

    @PostMapping("/add")
    public String addBus(@ModelAttribute Bus bus, RedirectAttributes ra) {
        try {
            Bus saved = busService.addBus(bus);
            ra.addFlashAttribute("message", "Bus '" + saved.getBusName() + "' added successfully!");
            return "redirect:/buses";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/buses/new";
        }
    }

    @PostMapping("/{id}/update")
    public String updateBus(@PathVariable Long id,
                            @ModelAttribute Bus bus,
                            RedirectAttributes ra) {
        try {
            busService.updateBus(id, bus);
            ra.addFlashAttribute("message", "Bus updated successfully.");
            return "redirect:/buses";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/buses/" + id + "/edit";
        }
    }

    // POST /buses/{id}/delete — deactivate bus
    @PostMapping("/{id}/delete")
    public String deleteBus(@PathVariable Long id, RedirectAttributes ra) {
        try {
            String msg = busService.deleteBus(id);
            ra.addFlashAttribute("message", msg);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/buses";
    }
}