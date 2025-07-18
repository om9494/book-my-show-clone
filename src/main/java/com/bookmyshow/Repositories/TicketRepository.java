package com.bookmyshow.Repositories;

import java.util.List;
import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookmyshow.Models.Ticket;
import com.bookmyshow.Models.User;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query(value = "Select * from tickets where show_show_id = ?1", nativeQuery = true)
    public List<Ticket> findByShowId(int showId);

    @Query(value = "Select * from tickets where user_id = ?1", nativeQuery = true)
    public List<Ticket> findByUserId(int userId);

    @Query(value = "Select * from tickets t where t.user_id = ?1 and show_show_id in (select show_id from shows where date >= ?2)", nativeQuery = true)
    public List<Ticket> findActiveTicketsByUserId(int userId, Date date);


}
