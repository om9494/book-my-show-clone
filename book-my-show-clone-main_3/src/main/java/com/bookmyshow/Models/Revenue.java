package com.bookmyshow.Models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int movieId;
    private double amount;
    private LocalDate date;

    public Revenue() {}
    public Revenue(int movieId, double amount, LocalDate date) {
        this.movieId = movieId;
        this.amount = amount;
        this.date = date;
    }

    // Getters & Setters
    public int getId() { return id; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
