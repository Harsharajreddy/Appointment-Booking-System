package com.appointment.bookingsystem.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.appointment.bookingsystem.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ✅ Fetch all bookings made by a specific user
    List<Booking> findByUserId(Long userId);

    // ✅ Check if an appointment is already booked
    boolean existsByAppointmentId(Long appointmentId);

    // ✅ Fetch booking by appointment ID for cancellation
    Optional<Booking> findByAppointmentId(Long appointmentId);
}
