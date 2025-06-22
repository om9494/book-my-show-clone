package com.bookmyshow.Repositories;

import com.bookmyshow.Models.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheatreRepository extends JpaRepository<Theatre, Integer> {
    List<Theatre> findByCityIgnoreCase(String city);
}


