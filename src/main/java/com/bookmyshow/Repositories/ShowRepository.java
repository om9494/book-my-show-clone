package com.bookmyshow.Repositories;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookmyshow.Models.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {
	
    @Query(value = "SELECT s.time FROM shows s WHERE s.date = ?1 AND s.theatre_id = ?2 AND s.movie_id = ?3", nativeQuery = true)
    public List<Time> getShowTimingsOnDate(Date date, Integer theaterId, Integer movieId);

    @Query(value = "SELECT * FROM shows WHERE movie_id = ?1 AND date = ?2 AND theatre_id = ?3", nativeQuery=true)
    public List<Show> getTheaterAndShowTimingsByMovie(Integer movieId, Date date, Integer theaterId);

    @Query(value = "SELECT s.movie_id FROM shows s GROUP BY s.movie_id ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    public Integer getMostShowsMovieId();

    //All shows of a movie in a specific theater
    @Query(value = "SELECT * FROM shows WHERE movie_id = ?1 AND theatre_id = ?2 AND date >= CURDATE()", nativeQuery = true)
    public List<Show> getAllShowsOfMovieInTheater(Integer movieId, Integer theaterId);

    //All shows for a movie
    @Query(value = "SELECT * FROM shows WHERE movie_id = ?1 AND date >= CURDATE()", nativeQuery = true)
    public List<Show> getAllShowsOfMovie(Integer movieId);

    //All shows 
    @Query(value = "SELECT * FROM shows WHERE date >= CURDATE()", nativeQuery = true)
    public List<Show> findAll();


}
