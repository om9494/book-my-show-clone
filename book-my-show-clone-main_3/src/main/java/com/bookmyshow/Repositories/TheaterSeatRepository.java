package com.bookmyshow.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookmyshow.Models.TheaterSeat;

@Repository
public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Integer> {

    @Query(value = "Select * from Theater_seats where theater_id = ?1", nativeQuery = true)
    public List<TheaterSeat> findAllByTheaterId(Integer id);
}
