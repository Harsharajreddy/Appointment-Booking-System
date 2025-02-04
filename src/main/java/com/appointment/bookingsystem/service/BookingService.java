package com.appointment.bookingsystem.service;

import com.appointment.bookingsystem.model.Appointment;
import com.appointment.bookingsystem.model.Booking;
import com.appointment.bookingsystem.model.User;
import com.appointment.bookingsystem.repository.AppointmentRepository;
import com.appointment.bookingsystem.repository.BookingRepository;
import com.appointment.bookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    // Book an appointment for a registered user
    public boolean bookForUser(Long appointmentId, Long userId, String guestName, String guestEmail, String guestPhone) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (appointmentOpt.isPresent() && userOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            User user = userOpt.get();

            if (appointment.getStatus() == Appointment.Status.AVAILABLE) {
                appointment.setStatus(Appointment.Status.BOOKED);
                appointmentRepository.save(appointment);

                // ✅ Store both user reference and user details in Booking
                Booking booking = Booking.builder()
                        .appointment(appointment)
                        .user(user)
                        .guestName(guestName != null ? guestName : user.getFullName()) // ✅ Set guest details correctly
                        .guestEmail(guestEmail != null ? guestEmail : user.getEmail())
                        .guestPhone(guestPhone != null ? guestPhone : user.getPhone())
                        .build();
                bookingRepository.save(booking);
                return true;
            }
        }
        return false;
    }


    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }


    // Book an appointment for a guest
    public boolean bookForGuest(Long appointmentId, String guestName, String guestPhone, String guestEmail) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();

            if (appointment.getStatus() == Appointment.Status.AVAILABLE) {
                appointment.setStatus(Appointment.Status.BOOKED);
                appointmentRepository.save(appointment);

                Booking booking = Booking.builder()
                        .appointment(appointment)
                        .guestName(guestName)
                        .guestPhone(guestPhone)
                        .guestEmail(guestEmail)
                        .build();
                bookingRepository.save(booking);
                return true;
            }
        }
        return false;
    }
    public boolean cancelBooking(Long appointmentId) {
        Optional<Booking> bookingOpt = bookingRepository.findByAppointmentId(appointmentId);

        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            Appointment appointment = booking.getAppointment();

            if (appointment == null) {
                System.err.println("❌ Appointment not found for Booking ID: " + booking.getId());
                return false;
            }

            // ✅ Mark the appointment as AVAILABLE again
            appointment.setStatus(Appointment.Status.AVAILABLE);
            appointmentRepository.save(appointment);

            // ✅ Delete the booking entry
            bookingRepository.delete(booking);
            System.out.println("✅ Booking for Appointment " + appointmentId + " canceled, slot now available");
            return true;
        }

        System.err.println("❌ No booking found for Appointment ID " + appointmentId);
        return false;
    }

}
