package com.bookmyshow.Models;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.sql.Date;
import jakarta.persistence.Enumerated;
import com.bookmyshow.Enums.Genre;
import com.bookmyshow.Enums.Language;
import jakarta.persistence.EnumType;




@Entity // Tells Spring this is a database table
@Table(name = "MOVIES") // Optional: sets table name explicitly
@Data // Lombok: generates getters, setters, equals, hashCode, toString
@Builder // Lombok: allows you to create objects using .builder() pattern
@NoArgsConstructor // Lombok: generates no-args constructor
@AllArgsConstructor // Lombok: generates all-args constructor
public class Movie {
	 @Id // Primary key
	    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
	    private Integer id;

	    @Column(nullable = false) // Can't be null in DB
	    private String movieName;

	    private Integer duration;

	    @Column(scale = 2)
	    private Double rating;

	    private Date releaseDate;

	    @Enumerated(value = EnumType.STRING) // Store enum as a string, not ordinal
	    private Genre genre;

	    @Enumerated(EnumType.STRING)
	    private Language language;

	    private String imageUrl; // 🎞️ NEW FIELD for poster/banner image

		
}


