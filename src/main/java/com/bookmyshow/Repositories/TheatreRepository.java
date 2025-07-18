package com.bookmyshow.Repositories;

import com.bookmyshow.Models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theater, Integer> {
    List<Theater> findByCityIgnoreCase(String city);

    @Query(value = "SELECT * FROM theaters WHERE city = ?1", nativeQuery=true)
    List<Theater> findByCity(String city);
}


