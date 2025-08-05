package com.bookmyshow.Repositories;

import java.util.List;


import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookmyshow.Models.SeatPrice;
import com.bookmyshow.Models.ShowSeat;

import java.util.Optional;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Integer> {

    @Query(value = "Select * from show_seats where show_id = ?1", nativeQuery = true)
    public List<ShowSeat> findByShowId(int showId);

    @Query(value = "Select * from show_seats where show_id = ?1 and is_available = true", nativeQuery = true)
    public List<ShowSeat> findAvailableSeatsByShowId(int showId);

    @Query(value = "Select * from show_seats where show_id = ?1 and is_available = false", nativeQuery = true)
    public List<ShowSeat> findBookedSeatsByShowId(int showId);

    @Query(value = "SELECT seat_type, price from show_seats where show_id=?1 AND seat_type='PREMIUM' UNION SELECT seat_type, price from show_seats where show_id=?1 AND seat_type='CLASSICPLUS' UNION SELECT seat_type, price from show_seats where show_id=?1 AND seat_type='CLASSIC'", nativeQuery = true)
    public List<SeatPrice> getSeatsPrices(Integer showId);
    
    Optional<ShowSeat> findById(int id);

}
