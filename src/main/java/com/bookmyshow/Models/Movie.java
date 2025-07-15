package com.bookmyshow.Models;

import com.bookmyshow.Enums.Genre;
import com.bookmyshow.Enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "MOVIES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Explicitly mapping the field to a specific column name
    @Column(name = "movie_name", nullable = false)
    private String movieName;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "rating", scale = 2)
    private Double rating;

    @Column(name = "release_date")
    private Date releaseDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "image_url")
    private String imageUrl;
}