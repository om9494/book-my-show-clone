package com.example.Super30_Project.Repository;

import com.example.Super30_Project.Entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheatreRepository extends JpaRepository<Theatre, Integer> {
    List<Theatre> findByCityIgnoreCase(String city);
}


