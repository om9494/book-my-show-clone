package com.bookmyshow.Models;

import java.time.LocalDateTime;

import com.bookmyshow.Enums.SeatType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "show_seats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String seatNo;
	
	@Enumerated(value = EnumType.STRING)
	private SeatType seatType;
	private Integer price;
	private Boolean isAvailable;
	private Boolean isFoodContains;
	// Add this new field
    private String lockedByUserId; // Stores the user ID who locked the seat
    private LocalDateTime lockedAt; // Stores the timestamp when the seat was locked
	
	@ManyToOne
    @JoinColumn(name = "show_id") 
	private Show show;
	
}
