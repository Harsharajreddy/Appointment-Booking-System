package com.appointment.bookingsystem.model;

import jakarta.persistence.*;
import lombok.*;
import com.appointment.bookingsystem.model.User;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;


    private String guestName;
    private String guestPhone;
    private String guestEmail;
}
