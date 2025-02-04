package com.appointment.bookingsystem.controller;

import com.appointment.bookingsystem.dto.BookingRequest;
import com.appointment.bookingsystem.model.Appointment;
import com.appointment.bookingsystem.model.Booking;
import com.appointment.bookingsystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/available")
    public List<Appointment> getAvailableAppointments() {
        return appointmentService.getAvailableAppointments();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentDetails(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/book/{id}")
    public ResponseEntity<?> bookAppointment(@PathVariable Long id, @RequestBody BookingRequest request) {
        boolean booked = appointmentService.bookAppointment(
                id,
                request.getUserId(),
                request.getGuestName(),
                request.getGuestEmail(),
                request.getGuestPhone()
        );

        if (booked) {
            return ResponseEntity.ok().body("Appointment booked successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Appointment already booked or not found");
        }
    }

    // ✅ Get all appointments booked by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserAppointments(@PathVariable Long userId) {
        List<Booking> appointments = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
        boolean deleted = appointmentService.deleteBooking(bookingId);

        if (deleted) {
            return ResponseEntity.ok("Booking deleted and slot is now available.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found.");
        }
    }

}
