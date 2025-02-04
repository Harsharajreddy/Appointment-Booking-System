package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.model.Booking;
import com.appointment.bookingsystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Book appointment for a registered user
    @PostMapping("/user")
    public String bookForUser(
            @RequestParam Long appointmentId,
            @RequestParam Long userId,
            @RequestParam String guestName,
            @RequestParam String guestEmail,
            @RequestParam String guestPhone) {

        boolean booked = bookingService.bookForUser(appointmentId, userId, guestName, guestEmail, guestPhone);
        return booked ? "Appointment booked successfully for user" : "Booking failed";
    }


    // Book appointment for a guest
    @PostMapping("/guest")
    public String bookForGuest(
            @RequestParam Long appointmentId,
            @RequestParam String guestName,
            @RequestParam String guestPhone,
            @RequestParam String guestEmail) {

        boolean booked = bookingService.bookForGuest(appointmentId, guestName, guestPhone, guestEmail);
        return booked ? "Appointment booked successfully for guest" : "Booking failed";
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
    @DeleteMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long appointmentId) {
        boolean canceled = bookingService.cancelBooking(appointmentId);
        if (canceled) {
            return ResponseEntity.ok("Booking canceled successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel booking");
        }
    }


}
