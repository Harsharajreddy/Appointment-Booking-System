package com.appointment.bookingsystem.service;

import com.appointment.bookingsystem.model.Appointment;
import com.appointment.bookingsystem.model.Booking;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import com.appointment.bookingsystem.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import com.appointment.bookingsystem.model.User;
import com.appointment.bookingsystem.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;

    // Generate available slots dynamically for the next 7 days
    public List<Appointment> getAvailableAppointments() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        System.out.println("Today's Date: " + today);  // ✅ Debug log
        System.out.println("Fetching appointments from " + today + " to " + nextWeek);
        // Fetch existing available appointments from DB
        List<Appointment> availableAppointments = appointmentRepository.findAvailableAppointments(today, nextWeek, Appointment.Status.AVAILABLE);
        System.out.println("Total Available Appointments: " + availableAppointments.size());

        // Generate missing slots if needed
        if (availableAppointments.size() < (7 * 9)) { // 7 days * 9 slots per day (9 AM - 6 PM)
            for (LocalDate currentDate = today; !currentDate.isAfter(nextWeek); currentDate = currentDate.plusDays(1)) {
                final LocalDate finalDate = currentDate; // Ensure effectively final variable
                for (int hour = 9; hour < 18; hour++) {
                    LocalTime time = LocalTime.of(hour, 0);
                    boolean exists = availableAppointments.stream()
                            .anyMatch(a -> a.getDate().equals(finalDate) && a.getTime().equals(time));

                    if (!exists) {
                        Appointment newSlot = new Appointment();
                        newSlot.setDate(finalDate);
                        newSlot.setTime(time);
                        newSlot.setStatus(Appointment.Status.AVAILABLE);
                        appointmentRepository.save(newSlot);
                        availableAppointments.add(newSlot);
                    }
                }
            }
        }
        return availableAppointments;
    }

    // Book an appointment
    public boolean bookAppointment(Long id, String userId, String guestName, String guestEmail, String guestPhone) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);

        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();

            // Check if already booked
            if (bookingRepository.existsByAppointmentId(id)) {
                return false; // Already booked
            }
            User user = null;
            if (userId != null && !userId.isEmpty()) {
                Optional<User> userOpt = userRepository.findById(Long.parseLong(userId));
                user = userOpt.orElse(null); // If user not found, set as null
            }
            // Update appointment status
            appointment.setStatus(Appointment.Status.BOOKED);
            appointmentRepository.save(appointment);

            // Save booking entry
            Booking newBooking = new Booking();
            newBooking.setAppointment(appointment);
            newBooking.setUser(user);
            newBooking.setGuestName(guestName);
            newBooking.setGuestEmail(guestEmail);
            newBooking.setGuestPhone(guestPhone);
            bookingRepository.save(newBooking);

            return true;
        }
        return false;
    }
    public List<Booking> getAppointmentsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    // In AppointmentService.java
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    

    @Transactional // ✅ Ensures atomic transaction
    public boolean deleteBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            Appointment appointment = booking.getAppointment(); // ✅ Get the appointment linked to the booking

            // ✅ Set the appointment status back to AVAILABLE
            appointment.setStatus(Appointment.Status.AVAILABLE);
            appointmentRepository.save(appointment);

            // ✅ Delete the booking entry
            bookingRepository.delete(booking);

            return true;
        }
        return false;
    }

}