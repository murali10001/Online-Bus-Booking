package com.busticket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long busId;

    @NotBlank(message = "Bus name is required")
    @Column(nullable = false)
    private String busName;

    @NotBlank(message = "Bus number is required")
    @Column(nullable = false, unique = true)
    private String busNumber;

    @NotBlank(message = "Source is required")
    @Column(nullable = false)
    private String source;

    @NotBlank(message = "Destination is required")
    @Column(nullable = false)
    private String destination;

    @NotNull(message = "Total seats required")
    @Min(value = 1, message = "Total seats must be at least 1")
    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @NotNull(message = "Ticket price is required")
    @Min(value = 0, message = "Price must be non-negative")
    @Column(nullable = false)
    private Double ticketPrice;

    @NotBlank(message = "Departure time is required")
    @Column(nullable = false)
    private String departureTime;

    @NotBlank(message = "Arrival time is required")
    @Column(nullable = false)
    private String arrivalTime;

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE / INACTIVE

    @JsonIgnore
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    @PrePersist
    public void initAvailableSeats() {
        if (this.availableSeats == null) {
            this.availableSeats = this.totalSeats;
        }
    }

    // Explicit getters/setters (to avoid IDE/Lombok issues)
    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}