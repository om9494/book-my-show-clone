package com.bookmyshow.Models;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;
    private Integer ticketPrice; // includes - Seat Price + Food Price + Other

    @CreationTimestamp
    private Date bookedAt;

    @ManyToOne
    @JoinColumn
    private Show show; // Booked Show in the theater

    @ManyToOne
    @JoinColumn
    private User user; // Booked User

    @OneToOne
    @JoinColumn
    private ShowSeat showSeat; // Booked Seat

}
