package com.bookmyshow.Repositories;

import com.bookmyshow.Models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheatreRepository extends JpaRepository<Theater, Integer> {
    List<Theater> findByCityIgnoreCase(String city);
}


