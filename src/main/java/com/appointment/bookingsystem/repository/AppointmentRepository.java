package com.appointment.bookingsystem.repository;

import org.springframework.data.repository.query.Param;
import com.appointment.bookingsystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.status = :status AND a.date BETWEEN :startDate AND :endDate")
    List<Appointment> findAvailableAppointments(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") Appointment.Status status
    );

}
