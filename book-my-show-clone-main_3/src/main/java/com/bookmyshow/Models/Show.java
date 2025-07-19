package com.bookmyshow.Models;

import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Entity;
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
@Table(name = "shows")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer showId;
	
	@CreationTimestamp
	private Time time;
	
	private Date date;
	
	@ManyToOne
    @JoinColumn
	private Movie movie;
	
	@ManyToOne
    @JoinColumn
	private Theater theatre;
}
