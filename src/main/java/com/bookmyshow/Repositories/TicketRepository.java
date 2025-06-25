package com.bookmyshow.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookmyshow.Models.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query(value = "Select * from tickets where show_show_id = ?1", nativeQuery = true)
    public List<Ticket> findByShowId(int showId);
}
